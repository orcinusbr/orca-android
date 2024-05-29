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

package br.com.orcinus.orca.core.mastodon.network.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData

/**
 * Allows for defining the response to be returned by a mock [HttpClient].
 *
 * @see provide
 */
internal fun interface ClientResponseProvider {
  /**
   * Provides [HttpResponseData] related to a response that's been given to a request.
   *
   * @param requestData [HttpResponseData] with characteristics of the request to which a response
   *   will be returned.
   */
  suspend fun MockRequestHandleScope.provide(requestData: HttpRequestData): HttpResponseData

  companion object {
    /**
     * [ClientResponseProvider] that provides a
     * [`200 OK`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/200) response.
     */
    val ok = ClientResponseProvider { respondOk() }
  }
}
