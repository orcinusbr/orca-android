/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination

import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.LinkHeader
import io.ktor.util.filter
import io.ktor.util.toMap

/** Returns a [List] containing only [LinkHeader]s. */
internal fun Headers.filterIsLink(): List<LinkHeader> {
  return filter { key, _ -> key.equals(HttpHeaders.Link, ignoreCase = true) }
    .toMap()
    .values
    .flatten()
    .map {
      LinkHeader(
        uri = it.substringAfter('<').substringBefore('>'),
        rel = it.substringAfter("rel=").substringBeforeLast('"')
      )
    }
}
