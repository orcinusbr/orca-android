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

package br.com.orcinus.orca.core.mastodon.network.requester.request

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.network.client.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.network.client.runUnauthenticatedTest
import br.com.orcinus.orca.core.mastodon.network.requester.Requester
import br.com.orcinus.orca.core.mastodon.network.requester.request.memory.InMemoryRequestDao
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.respondOk
import io.mockk.spyk
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Runs a [Requester]-focused test targeting request resumption behavior.
 *
 * @param request Performs the request to be performed, interrupted and then resumed.
 * @param onResumption Callback called after the [request] has been resumed.
 */
@OptIn(ExperimentalContracts::class)
internal fun runRequesterResumptionTest(
  request: suspend Requester.() -> Unit,
  onResumption: suspend Requester.() -> Unit
) {
  contract {
    callsInPlace(request, InvocationKind.AT_LEAST_ONCE)
    callsInPlace(onResumption, InvocationKind.EXACTLY_ONCE)
  }
  runUnauthenticatedRequesterTest(
    onAuthentication = {},
    clientResponseProvider = {
      delay(2.minutes)
      respondOk()
    }
  ) {
    launch(Dispatchers.Unconfined) { it.request() }.cancel()
    it.interrupt()
    launch(Dispatchers.Unconfined) { it.resume() }.cancel()
    it.onResumption()
  }
}

/**
 * Runs a [Requester]-focused test.
 *
 * @param onAuthentication Action run whenever the [Actor] is authenticated.
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param body Operation to be performed with the [Requester].
 */
@OptIn(ExperimentalContracts::class)
internal fun runUnauthenticatedRequesterTest(
  onAuthentication: () -> Unit,
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  body: suspend CoroutineScope.(Requester) -> Unit
) {
  contract {
    callsInPlace(onAuthentication)
    callsInPlace(body, InvocationKind.EXACTLY_ONCE)
  }
  runUnauthenticatedTest(onAuthentication, clientResponseProvider) {
    val requestDao = InMemoryRequestDao()
    val requester = Requester(client, authenticationLock, requestDao)
    val spiedRequester = spyk(requester)
    try {
      body(spiedRequester)
    } finally {
      requestDao.clear()
    }
  }
}
