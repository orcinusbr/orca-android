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

import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import org.junit.Assert.assertEquals
import org.junit.Test

internal class HeadersExtensionsTests {
  @Test
  fun `GIVEN String Link headers WHEN converting each into a LinkHeader THEN they're converted`() {
    val headers =
      headers {
          append(HttpHeaders.Link, """<https://example.com/next>; rel="next"""")
          append(HttpHeaders.Link, """<https://example.com/previous>; rel="previous"""")
        }
        .links
    assertEquals("https://example.com/next", headers.first().uri)
    assertEquals("https://example.com/previous", headers[1].uri)
    assertEquals("next", headers.first().parameter("rel"))
    assertEquals("previous", headers[1].parameter("rel"))
  }
}
