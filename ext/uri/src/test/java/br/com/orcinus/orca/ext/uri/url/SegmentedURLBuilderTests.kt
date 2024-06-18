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

package br.com.orcinus.orca.ext.uri.url

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.ext.uri.URIBuilder
import java.net.URI
import kotlin.test.Test

internal class SegmentedURLBuilderTests {
  @Test
  fun appendsParameters() {
    assertThat(
        URIBuilder.url()
          .scheme("https")
          .host("mastodon.social")
          .path("api")
          .path("v1")
          .path("statuses")
          .path("112276588128366269")
          .path("reblogged_by")
          .query()
          .parameter("limit", "2")
          .parameter("since_id", "112276666465473478")
          .build()
      )
      .isEqualTo(
        URI(
          "https://mastodon.social/api/v1/statuses/112276588128366269/reblogged_by?limit=2&since_" +
            "id=112276666465473478"
        )
      )
  }
}
