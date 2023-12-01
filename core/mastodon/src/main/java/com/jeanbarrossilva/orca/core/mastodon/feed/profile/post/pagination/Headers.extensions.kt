/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
