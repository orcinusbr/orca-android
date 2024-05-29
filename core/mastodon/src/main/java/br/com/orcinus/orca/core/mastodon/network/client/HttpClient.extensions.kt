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
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

/**
 * Configures [Json] serialization behavior on content negotiation, ignoring unknown keys and
 * mapping known ones' names to snake case.
 */
@InternalNetworkApi
internal fun HttpClientConfig<*>.normalizeJsonKeys() {
  install(ContentNegotiation) {
    json(
      Json {
        ignoreUnknownKeys = true

        @OptIn(ExperimentalSerializationApi::class)
        namingStrategy = JsonNamingStrategy.SnakeCase
      }
    )
  }
}
