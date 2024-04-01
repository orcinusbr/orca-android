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
