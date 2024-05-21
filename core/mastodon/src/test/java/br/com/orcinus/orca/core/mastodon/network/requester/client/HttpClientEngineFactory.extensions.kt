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

package br.com.orcinus.orca.core.mastodon.network.requester.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse

/**
 * Creates an [HttpClientEngineFactory] that produces a [MockEngine].
 *
 * @param clientResponseProvider [ClientResponseProvider] that defines the [HttpResponse] to be
 *   provided to an [HttpRequest].
 */
internal fun createHttpClientEngineFactory(
  clientResponseProvider: ClientResponseProvider
): HttpClientEngineFactory<MockEngineConfig> {
  return object : HttpClientEngineFactory<MockEngineConfig> {
    override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine {
      return MockEngine { with(clientResponseProvider) { provide(it) } }.apply { config.block() }
    }
  }
}
