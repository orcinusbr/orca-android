/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.network.requester.client

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.mastodon.MastodonCoreModule
import br.com.orcinus.orca.core.mastodon.instance.TestMastodonInstanceProvider
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.test.TestActorProvider
import br.com.orcinus.orca.core.test.TestAuthenticator
import br.com.orcinus.orca.core.test.TestAuthorizer
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequest
import io.ktor.http.HttpMethod
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/**
 * [CoroutineScope] created from the [TestScope] in which the test in running, that houses
 * [MastodonClient]-test-related structures.
 *
 * @param T Specified [Actor] for performing the testing.
 * @param delegate [TestScope] that's been launched and will provide [CoroutineScope]-like
 *   functionality to this [MastodonClientTestScope].
 * @param authenticationLock [AuthenticationLock] by which authentication might have been required.
 * @param client [MastodonClient] for executing the intended [HttpRequest]s.
 * @param actor [Actor] used when running the test.
 */
internal class MastodonClientTestScope<T : Actor>(
  delegate: TestScope,
  val client: HttpClient,
  val authenticationLock: AuthenticationLock<TestAuthenticator>,
  val actor: T
) : CoroutineScope by delegate

/**
 * [ActorProvider] that provides only the specified [actor].
 *
 * @param actor [Actor] to be provided unconditionally.
 */
internal class FixedActorProvider(private val actor: Actor) : TestActorProvider() {
  override suspend fun remember(actor: Actor) {}

  override suspend fun retrieve(): Actor {
    return actor
  }
}

/**
 * Configures an environment for a [MastodonClient] test with an
 * [authenticated][Actor.Authenticated] [Actor], providing the proper [MastodonClientTestScope].
 *
 * @param body Callback run when the environment has been set up and is, therefore, ready to be
 *   used.
 */
@OptIn(ExperimentalContracts::class)
internal fun runAuthenticatedTest(
  body: suspend MastodonClientTestScope<Actor.Authenticated>.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  runCoreHttpClientTest(Actor.Authenticated.sample, onAuthentication = {}, body = body)
}

/**
 * Configures an environment for a [MastodonClient] test with an
 * [unauthenticated][Actor.Unauthenticated] [Actor], providing the proper [MastodonClientTestScope].
 *
 * @param onAuthentication Action run whenever the [Actor] is authenticated.
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param body Callback run when the environment has been set up and is, therefore, ready to be
 *   used.
 */
@OptIn(ExperimentalContracts::class)
internal fun runUnauthenticatedTest(
  onAuthentication: () -> Unit,
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  body: suspend MastodonClientTestScope<Actor.Unauthenticated>.() -> Unit
) {
  contract {
    callsInPlace(onAuthentication, InvocationKind.AT_MOST_ONCE)
    callsInPlace(body, InvocationKind.EXACTLY_ONCE)
  }
  runCoreHttpClientTest(Actor.Unauthenticated, onAuthentication, clientResponseProvider, body)
}

/**
 * Configures an environment for a [MastodonClient] test, providing the proper
 * [MastodonClientTestScope].
 *
 * @param T [Actor] to run the test with.
 * @param actor [Actor] to be fixedly provided by the underlying [ActorProvider]. Determines the
 *   behavior of `authenticateAnd*` calls done on the [MastodonClient] within the [body], given that
 *   an [unauthenticated][Actor.Unauthenticated] [Actor] will be requested to be authenticated when
 *   such invocations take place, while having an [authenticated][Actor.Authenticated] [Actor] would
 *   simply mean that the operation derived from the [HttpMethod] will be performed.
 * @param onAuthentication Action run whenever the [Actor] is authenticated. At this point, the most
 *   up-to-date [Actor] is probably different from the one with which the test has been configured
 *   and that is in the [MastodonClientTestScope] given to the [body].
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param body Callback run when the environment has been set up and is, therefore, ready to be
 *   used.
 * @see HttpClient.authenticateAndGet
 * @see HttpClient.authenticateAndPost
 * @see HttpClient.authenticateAndSubmitForm
 * @see HttpClient.authenticateAndSubmitFormWithBinaryData
 * @see MastodonClientTestScope.client
 */
@OptIn(ExperimentalContracts::class)
private fun <T : Actor> runCoreHttpClientTest(
  actor: T,
  onAuthentication: () -> Unit,
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  body: suspend MastodonClientTestScope<T>.() -> Unit
) {
  contract {
    callsInPlace(onAuthentication, InvocationKind.AT_MOST_ONCE)
    callsInPlace(body, InvocationKind.EXACTLY_ONCE)
  }
  val authorizer = TestAuthorizer()
  val actorProvider = FixedActorProvider(actor)
  val authenticator = TestAuthenticator(authorizer, actorProvider) { onAuthentication() }
  val authenticationLock = AuthenticationLock(authenticator, actorProvider)
  val instanceProvider =
    TestMastodonInstanceProvider(
      authorizer,
      authenticator,
      authenticationLock,
      clientResponseProvider
    )
  val module =
    MastodonCoreModule(
      lazyInjectionOf { instanceProvider },
      lazyInjectionOf { authenticationLock },
      lazyInjectionOf { SampleTermMuter() }
    )
  val instance = instanceProvider.provide()
  Injector.register<CoreModule>(module)
  runTest {
    try {
      MastodonClientTestScope(delegate = this, instance.client, authenticationLock, actor).body()
    } finally {
      Injector.unregister<CoreModule>()
    }
  }
}
