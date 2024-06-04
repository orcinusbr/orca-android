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

package br.com.orcinus.orca.core.mastodon.network.requester

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import br.com.orcinus.orca.core.mastodon.network.client.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.network.client.MastodonClientTestScope
import br.com.orcinus.orca.core.mastodon.network.client.createHttpClientEngineFactory
import br.com.orcinus.orca.core.mastodon.network.client.runUnauthenticatedTest
import br.com.orcinus.orca.std.uri.URIBuilder
import br.com.orcinus.orca.std.uri.url.HostedURLBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respondError
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import java.net.URI
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CoroutineScope

/**
 * [CoroutineScope] from which [Requester]-related structures can be referenced from tests.
 *
 * @param T [Requester] within this [CoroutineScope].
 * @property delegate [MastodonClientTestScope] to which [CoroutineScope]-like functionality will be
 *   delegated.
 * @property requester [Requester] that's been created.
 * @property route Produces a default route from the [requester]'s base [URI] to which the
 *   [requester]'s requests can be sent.
 */
@InternalNetworkApi
internal class RequesterTestScope<T : Requester>(
  val delegate: MastodonClientTestScope<*>,
  val requester: T,
  val route: URI.() -> URI
) : CoroutineScope by delegate

/**
 * Obtains the amount of times the [request] is retried.
 *
 * @param request Performs the request to be retried.
 */
@InternalNetworkApi
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
 * @param body Operation to be performed with the [Requester].
 */
@InternalNetworkApi
@OptIn(ExperimentalContracts::class)
internal inline fun runRequesterTest(
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  crossinline onAuthentication: () -> Unit = {},
  crossinline body: suspend RequesterTestScope<Requester>.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  runUnauthenticatedTest(onAuthentication, clientResponseProvider) {
    val baseURI = URIBuilder.url().scheme("https").host("orca.orcinus.com.br").path("app").build()
    val clientEngineFactory = createHttpClientEngineFactory<MockEngineConfig>(client::engine)
    val requester = Requester(NoOpLogger, baseURI, clientEngineFactory)
    RequesterTestScope(this, requester) {
        HostedURLBuilder.from(this).path("api").path("v1").path("resource").build()
      }
      .body()
  }
}
