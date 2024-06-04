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

package br.com.orcinus.orca.core.mastodon.network.requester.resumption

import androidx.annotation.IntRange
import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import br.com.orcinus.orca.core.mastodon.network.client.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.network.requester.CountingClientResponseProvider
import br.com.orcinus.orca.core.mastodon.network.requester.RequesterTestScope
import br.com.orcinus.orca.core.mastodon.network.requester.resumption.request.memory.InMemoryRequestDao
import br.com.orcinus.orca.core.mastodon.network.requester.runRequesterTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.statement.HttpResponse
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.currentTime

/**
 * Obtains the amount of times the [request] has been actually resumed after it's been interrupted.
 *
 * @param request Performs the request to be performed, interrupted and then attempted to be
 *   resumed.
 */
@IntRange(from = 0, to = 1)
@InternalNetworkApi
@OptIn(ExperimentalContracts::class)
internal inline fun resumptionCountOf(
  crossinline request: suspend RequesterTestScope<ResumableRequester>.() -> Unit
): Int {
  contract { callsInPlace(request, InvocationKind.AT_LEAST_ONCE) }
  lateinit var testScheduler: TestCoroutineScheduler
  val clientResponseProvider = CountingClientResponseProvider {
    delay(ResumableRequester.timeToLive)
    respondOk()
  }
  runResumableRequesterTest(clientResponseProvider) {
    val testScope = delegate.delegate

    @OptIn(ExperimentalCoroutinesApi::class)
    val jobContext = UnconfinedTestDispatcher(testScope.testScheduler)

    testScheduler = testScope.testScheduler
    val requestJob = launch(jobContext) { request() }
    clientResponseProvider.waitUntilCountIs(1)
    requester.interrupt()
    requestJob.cancelAndJoin()
    testScheduler.advanceTimeBy(ResumableRequester.timeToLive + 1.milliseconds)
    val resumptionJob = launch(jobContext) { requester.resume() }
    clientResponseProvider.waitUntilCountIs(2)
    resumptionJob.cancel()
  }
  return clientResponseProvider.count.dec()
}

/**
 * Obtains the amount of times an [HttpResponse] is provided for the [request].
 *
 * @param request Performs the request.
 */
@InternalNetworkApi
@OptIn(ExperimentalContracts::class)
internal inline fun responseCountOf(
  crossinline request: suspend RequesterTestScope<ResumableRequester>.() -> HttpResponse
): Int {
  contract { callsInPlace(request, InvocationKind.EXACTLY_ONCE) }
  val clientResponseProvider = CountingClientResponseProvider(ClientResponseProvider.ok)
  runResumableRequesterTest(clientResponseProvider) { request() }
  return clientResponseProvider.count
}

/**
 * Runs a [ResumableRequester]-focused test.
 *
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param body Operation to be performed with the [ResumableRequester].
 */
@InternalNetworkApi
@OptIn(ExperimentalContracts::class)
private inline fun runResumableRequesterTest(
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  crossinline body: suspend RequesterTestScope<ResumableRequester>.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  runRequesterTest(clientResponseProvider) {
    val requestDao = InMemoryRequestDao()
    val requester =
      requester.resumable(
        { @OptIn(ExperimentalCoroutinesApi::class) delegate.delegate.currentTime.milliseconds },
        requestDao
      )
    try {
      body(RequesterTestScope(delegate, requester, route))
    } finally {
      requestDao.clear()
      requester.interrupt()
    }
  }
}
