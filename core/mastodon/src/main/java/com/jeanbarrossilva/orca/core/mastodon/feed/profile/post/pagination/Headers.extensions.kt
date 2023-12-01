/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination

import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.LinkHeader
import io.ktor.util.filter
import io.ktor.util.toMap

/**
 * Parametrized URIs present in the Link header, each represented by a [LinkHeader].
 *
 * @see HttpHeaders.Link
 */
internal val Headers.links
  get() =
    filter { key, _ -> key.equals(HttpHeaders.Link, ignoreCase = true) }
      .toMap()
      .values
      .flatten()
      .map {
        LinkHeader(
          uri = it.substringAfter('<').substringBefore('>'),
          rel = it.substringAfter("rel=\"").substringBeforeLast('"')
        )
      }
