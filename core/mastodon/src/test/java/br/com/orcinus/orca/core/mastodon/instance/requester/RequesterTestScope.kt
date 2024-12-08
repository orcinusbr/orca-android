/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.instance.requester

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.MastodonCoreModule
import br.com.orcinus.orca.core.mastodon.instance.SampleMastodonInstanceProvider
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.test.auth.AuthenticationLock
import br.com.orcinus.orca.core.test.auth.Authenticator
import br.com.orcinus.orca.core.test.auth.AuthorizerBuilder
import br.com.orcinus.orca.core.test.auth.actor.InMemoryActorProvider
import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.respondError
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import java.net.URI
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/**
 * [CoroutineScope] from which [Requester]-related structures can be referenced from tests.
 *
 * @param T [Requester] within this [CoroutineScope].
 * @property delegate [TestScope] to which [CoroutineScope]-like functionality will be delegated.
 * @property requester [Requester] that's been created.
 * @property route Produces a default route from the [requester]'s base [URI] to which the requests
 *   can be sent.
 */
@InternalRequesterApi
internal class RequesterTestScope<T : Requester>(
  val delegate: TestScope,
  val requester: T,
  val route: HostedURLBuilder.() -> URI
) : CoroutineScope by delegate

/**
 * Obtains the amount of times the [request] is retried.
 *
 * @param request Performs the request to be retried.
 */
@InternalRequesterApi
@OptIn(ExperimentalContracts::class)
internal inline fun retryCountOf(
  crossinline request: suspend RequesterTestScope<Requester>.() -> HttpResponse
): Int {
  contract { callsInPlace(request, InvocationKind.EXACTLY_ONCE) }
  val clientResponseProvider = CountingClientResponseProvider {
    respondError(HttpStatusCode.NotImplemented)
  }
  runRequesterTest(clientResponseProvider) { request() }
  return clientResponseProvider.count.dec()
}

/**
 * Runs a [Requester]-focused test.
 *
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param onAuthentication Action run whenever the [Actor] is authenticated.
 * @param context [CoroutineContext] in which [Job]s are launched by default.
 * @param body Operation to be performed with the [Requester].
 */
@OptIn(ExperimentalContracts::class)
internal inline fun runRequesterTest(
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  crossinline onAuthentication: () -> Unit = {},
  context: CoroutineContext = EmptyCoroutineContext,
  crossinline body: suspend RequesterTestScope<Requester>.() -> Unit
) {
  contract {
    callsInPlace(onAuthentication, InvocationKind.AT_MOST_ONCE)
    callsInPlace(body, InvocationKind.EXACTLY_ONCE)
  }
  val authorizer = AuthorizerBuilder().build()
  val actorProvider = InMemoryActorProvider()
  val authenticator =
    Authenticator(actorProvider, authorizer) {
      onAuthentication()
      Actor.Authenticated.sample
    }
  val authenticationLock = AuthenticationLock(authenticator, actorProvider)
  val instanceProvider =
    SampleMastodonInstanceProvider(authorizer, authenticator, authenticationLock)
  val module =
    MastodonCoreModule(
      lazyInjectionOf { instanceProvider },
      lazyInjectionOf { authenticationLock },
      lazyInjectionOf { SampleTermMuter() }
    )
  Injector.register<CoreModule>(module)
  runTest(context) {
    try {
      val baseURI = URIBuilder.url().scheme("https").host("orca.orcinus.com.br").path("app").build()
      val clientEngineFactory = createHttpClientEngineFactory(clientResponseProvider)
      val requester = Requester(NoOpLogger, baseURI, clientEngineFactory)
      RequesterTestScope(this, requester) { path("api").path("v1").path("resource").build() }.body()
    } finally {
      Injector.unregister<CoreModule>()
    }
  }
}
