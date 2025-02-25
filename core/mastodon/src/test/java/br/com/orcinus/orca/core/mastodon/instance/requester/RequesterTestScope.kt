/*
 * Copyright © 2024–2025 Orcinus
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

import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
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
 * Engine factory whose response is delegated to a [ClientResponseProvider].
 *
 * @property delegate Defines the response to be provided to an HTTP request.
 */
private class DelegatorHttpClientEngineFactory(private val delegate: ClientResponseProvider) :
  HttpClientEngineFactory<MockEngineConfig> {
  override fun create(block: MockEngineConfig.() -> Unit) =
    MockEngine { with(delegate) { provide(it) } }.apply { config.block() }

  companion object {
    /** Factory of an engine which responds to all requests with OK. */
    val ok = DelegatorHttpClientEngineFactory(ClientResponseProvider.ok)
  }
}

/**
 * [CoroutineScope] from which [Requester]-related structures can be referenced from tests.
 *
 * @param T [Requester] within this [CoroutineScope].
 * @property delegate [TestScope] to which [CoroutineScope]-like functionality will be delegated.
 * @property requester [Requester] that's been created.
 */
@InternalRequesterApi
internal class RequesterTestScope<T : Requester>(val delegate: TestScope, val requester: T) :
  CoroutineScope by delegate {
  /**
   * Produces a default route from the [requester]'s base [URI] to which the requests can be sent.
   */
  val route: HostedURLBuilder.() -> URI = { path("api").path("v1").path("resource").build() }

  companion object {
    /** Default, fictional base URI of a [Requester] being tested. */
    val baseURI = URIBuilder.url().scheme("https").host("orca.orcinus.com.br").path("app").build()
  }
}

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
 * @param context [CoroutineContext] in which [Job]s are launched by default.
 * @param body Operation to be performed with the [Requester].
 */
@OptIn(ExperimentalContracts::class)
internal inline fun runRequesterTest(
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  context: CoroutineContext = EmptyCoroutineContext,
  crossinline body: suspend RequesterTestScope<Requester>.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  val requester =
    Requester(
      NoOpLogger,
      RequesterTestScope.baseURI,
      httpClientEngineFactoryOf(clientResponseProvider)
    )
  runTest(context) { RequesterTestScope(this, requester).body() }
}

/**
 * Returns a factory that produces a mocked engine.
 *
 * @param responseProvider Defines the response to be provided to an HTTP request.
 * @see HttpClientEngineFactory.create
 */
@InternalRequesterApi
internal fun httpClientEngineFactoryOf(
  responseProvider: ClientResponseProvider
): HttpClientEngineFactory<MockEngineConfig> =
  if (responseProvider === ClientResponseProvider.ok) {
    DelegatorHttpClientEngineFactory.ok
  } else {
    DelegatorHttpClientEngineFactory(responseProvider)
  }
