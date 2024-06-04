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

import br.com.orcinus.orca.core.mastodon.network.InternalNetworkApi
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse

/**
 * Creates an [HttpClientEngineFactory] that produces a [MockEngine].
 *
 * @param responseProvider [ClientResponseProvider] that defines the [HttpResponse] to be provided
 *   to an [HttpRequest].
 * @see HttpClientEngineFactory.create
 */
@InternalNetworkApi
internal fun createHttpClientEngineFactory(
  responseProvider: ClientResponseProvider
): HttpClientEngineFactory<MockEngineConfig> {
  return createHttpClientEngineFactory { MockEngine { with(responseProvider) { provide(it) } } }
}

/**
 * Creates an [HttpClientEngineFactory] that produces the [engine].
 *
 * @param T [HttpClientEngineConfig] with which the [engine] is configured.
 * @param engine Returns [HttpClientEngine] to be created by the [HttpClientEngineFactory] when it's
 *   requested to produce a new one.
 * @throws ClassCastException If the [engine]'s configuration isn't of type [T].
 * @see HttpClientEngine.config
 * @see HttpClientEngineFactory.create
 */
@InternalNetworkApi
@Throws(ClassCastException::class)
internal fun <T : HttpClientEngineConfig> createHttpClientEngineFactory(
  engine: () -> HttpClientEngine
): HttpClientEngineFactory<T> {
  return object : HttpClientEngineFactory<T> {
    override fun create(block: T.() -> Unit): HttpClientEngine {
      return engine().apply { @Suppress("UNCHECKED_CAST") (config as T).block() }
    }
  }
}
