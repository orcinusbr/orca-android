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

package br.com.orcinus.orca.core.mastodon.instance.requester.resumption

import androidx.annotation.IntRange
import br.com.orcinus.orca.core.mastodon.instance.requester.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.instance.requester.CountingClientResponseProvider
import br.com.orcinus.orca.core.mastodon.instance.requester.InternalRequesterApi
import br.com.orcinus.orca.core.mastodon.instance.requester.RequesterTestScope
import br.com.orcinus.orca.core.mastodon.instance.requester.runRequesterTest
import br.com.orcinus.orca.std.func.monad.Maybe
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

/**
 * Obtains the amount of times the [request] has been actually resumed after it's been interrupted.
 *
 * @param request Performs the request to be performed, interrupted and then attempted to be
 *   resumed.
 */
@IntRange(from = 0, to = 1)
@InternalRequesterApi
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
    testScheduler = delegate.testScheduler

    @OptIn(ExperimentalCoroutinesApi::class)
    val jobContext = UnconfinedTestDispatcher(testScheduler)

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
@InternalRequesterApi
@OptIn(ExperimentalContracts::class)
internal inline fun responseCountOf(
  crossinline request: suspend RequesterTestScope<ResumableRequester>.() -> Maybe<*, HttpResponse>
): Int {
  contract { callsInPlace(request, InvocationKind.EXACTLY_ONCE) }
  val clientResponseProvider = CountingClientResponseProvider(ClientResponseProvider.ok)
  runResumableRequesterTest(clientResponseProvider) { request().getValueOrThrow() }
  return clientResponseProvider.count
}

/**
 * Runs a [ResumableRequester]-focused test.
 *
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param body Operation to be performed with the [ResumableRequester].
 */
@InternalRequesterApi
@OptIn(ExperimentalContracts::class)
private inline fun runResumableRequesterTest(
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  crossinline body: suspend RequesterTestScope<ResumableRequester>.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  runRequesterTest(clientResponseProvider) {
    val requester = requester.resumable(delegate)
    val requesterScope = RequesterTestScope(delegate, requester)
    try {
      requesterScope.body()
    } finally {
      requester.requestDao.clear()
      requester.interrupt()
    }
  }
}
