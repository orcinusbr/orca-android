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

import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import br.com.orcinus.orca.core.mastodon.network.client.ClientResponseProvider
import br.com.orcinus.orca.ext.coroutines.getValue
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take

/**
 * [ClientResponseProvider] that counts the provided responses.
 *
 * @property delegate [ClientResponseProvider] to which provision is delegated.
 * @see count
 */
@InternalNetworkApi
internal class CountingClientResponseProvider(private val delegate: ClientResponseProvider) :
  ClientResponseProvider {
  /** [MutableStateFlow] to which the amount of times a response has been provided is emitted. */
  private val countFlow = MutableStateFlow(0)

  /** Amount of times a response has been provided. */
  val count by countFlow

  override suspend fun MockRequestHandleScope.provide(
    requestData: HttpRequestData
  ): HttpResponseData {
    countFlow.value++
    return with(delegate) { provide(requestData) }
  }

  /**
   * Suspends until a [count] amount of responses has been provided.
   *
   * @param count Response quantity to there be for the execution flow to be resumed.
   */
  suspend fun waitUntilCountIs(count: Int) {
    countFlow.filter(count::equals).take(1).collect()
  }
}
