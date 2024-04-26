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

package br.com.orcinus.orca.std.uri.url

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.std.uri.URIBuilder
import java.net.URI
import kotlin.test.Test

internal class HostedURLBuilderTests {
  @Test
  fun createsHostedURLBuilder() {
    assertThat(
        HostedURLBuilder.from(URIBuilder.url().scheme("https").host("mastodon.social").build())
      )
      .isEqualTo(HostedURLBuilder("https", "mastodon.social"))
  }

  @Test
  fun appendsPaths() {
    assertThat(
        URIBuilder.url()
          .scheme("https")
          .host("mastodon.social")
          .path("@jeanbarrossilva")
          .path("following")
          .build()
      )
      .isEqualTo(URI.create("https://mastodon.social/@jeanbarrossilva/following"))
  }

  @Test
  fun createsSegmentedURLBuilder() {
    assertThat(
        URIBuilder.url()
          .scheme("https")
          .host("mastodon.social")
          .path("@jeanbarrossilva")
          .path("followers")
          .query()
      )
      .isEqualTo(SegmentedURLBuilder("https", "mastodon.social", "/@jeanbarrossilva/followers"))
  }
}
