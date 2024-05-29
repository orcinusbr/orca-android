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

package br.com.orcinus.orca.core.mastodon.network.request

import br.com.orcinus.orca.core.mastodon.network.Requester
import br.com.orcinus.orca.core.mastodon.network.runUnauthenticatedRequesterTest
import io.ktor.client.engine.mock.respondOk
import io.mockk.coVerify
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Obtains the resumption policy that's actually been applied after attempting to resume the
 * [request] after it's been interrupted.
 *
 * @param request Performs the request to be performed, interrupted and then attempted to be
 *   resumed.
 * @return [Resumption.None] when the [request] hasn't been resumed or [Resumption.Resumable] when
 *   it has been.
 * @throws IllegalStateException If the [request] isn't performed or is but more than twice.
 */
@OptIn(ExperimentalContracts::class)
@Throws(IllegalStateException::class)
internal inline fun resumptionOf(crossinline request: suspend Requester.() -> Unit): Resumption {
  contract { callsInPlace(request, InvocationKind.AT_LEAST_ONCE) }
  lateinit var resumption: Resumption
  runUnauthenticatedRequesterTest(
    onAuthentication = {},
    clientResponseProvider = {
      delay(2.milliseconds)
      respondOk()
    }
  ) {
    launch(Dispatchers.Unconfined) { it.request() }.cancel()
    it.interrupt()
    launch(Dispatchers.Unconfined) { it.resume() }.cancel()
    resumption =
      try {
        coVerify(exactly = 0) { it.request() }
        throw IllegalStateException("Request hasn't been performed.")
      } catch (_: AssertionError) {
        try {
          coVerify(exactly = 1) { it.request() }
          Resumption.None
        } catch (_: AssertionError) {
          try {
            coVerify(exactly = 2) { it.request() }
            Resumption.Resumable
          } catch (_: AssertionError) {
            throw IllegalStateException("Request has been performed more than twice.")
          }
        }
      }
  }
  return resumption
}
