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

package br.com.orcinus.orca.core.mastodon.network

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.network.client.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.network.client.NoOpLogger
import br.com.orcinus.orca.core.mastodon.network.client.runUnauthenticatedTest
import br.com.orcinus.orca.core.mastodon.network.request.memory.InMemoryRequestDao
import br.com.orcinus.orca.std.uri.URIBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respondError
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.spyk
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CoroutineScope

/**
 * Obtains the amount of times the [request] is retried.
 *
 * @param request Performs the request to be retried.
 */
@OptIn(ExperimentalContracts::class)
internal inline fun retryCountOf(crossinline request: suspend Requester.() -> HttpResponse): Int {
  contract { callsInPlace(request, InvocationKind.EXACTLY_ONCE) }
  var requestCount = 0
  runUnauthenticatedRequesterTest(
    onAuthentication = {},
    clientResponseProvider = {
      requestCount++
      respondError(HttpStatusCode.NotImplemented)
    }
  ) {
    it.request()
  }
  return requestCount.dec()
}

/**
 * Runs a [Requester]-focused test.
 *
 * @param onAuthentication Action run whenever the [Actor] is authenticated.
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param body Operation to be performed with the [Requester].
 */
@OptIn(ExperimentalContracts::class)
internal inline fun runUnauthenticatedRequesterTest(
  crossinline onAuthentication: () -> Unit,
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  crossinline body: suspend CoroutineScope.(Requester) -> Unit
) {
  contract {
    callsInPlace(onAuthentication)
    callsInPlace(body, InvocationKind.EXACTLY_ONCE)
  }
  runUnauthenticatedTest(onAuthentication, clientResponseProvider) {
    val clientEngineFactory =
      object : HttpClientEngineFactory<MockEngineConfig> {
        override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine {
          return (client.engine as MockEngine).apply { config.block() }
        }
      }
    val requestDao = InMemoryRequestDao()
    val base = URIBuilder.url().scheme("https").host("orca.orcinus.com.br").path("app").build()
    val requester = Requester(authenticationLock, clientEngineFactory, NoOpLogger, requestDao, base)
    val spiedRequester = spyk(requester)
    try {
      body(spiedRequester)
    } finally {
      requestDao.clear()
      requester.interrupt()
    }
  }
}
