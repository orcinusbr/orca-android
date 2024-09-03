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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.pagination

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import assertk.assertions.single
import br.com.orcinus.orca.ext.testing.hasPropertiesEqualToThoseOf
import br.com.orcinus.orca.ext.uri.URIBuilder
import io.ktor.http.HttpHeaders
import io.ktor.http.LinkHeader
import io.ktor.http.headersOf
import kotlin.test.Test

internal class HeadersExtensionsTests {
  private val linkHeader =
    LinkHeader(
      "${URIBuilder.url().scheme("https").host("orca.orcinus.com.br").build()}",
      LinkHeader.Rel.Next
    )

  @Test
  fun filtersIsLink() {
    assertThat(
        headersOf(
            HttpHeaders.Authorization to listOf("Bearer 123"),
            HttpHeaders.Link to listOf("$linkHeader")
          )
          .filterIsLink()
      )
      .single()
      .hasPropertiesEqualToThoseOf(linkHeader)
  }

  @Test
  fun filtersIsLinkWhenRelIsNotSurroundedByQuotes() {
    assertThat(
        headersOf(
            HttpHeaders.Authorization to listOf("Bearer 123"),
            HttpHeaders.Link to
              listOf("<${linkHeader.uri}>; ${LinkHeader.Parameters.Rel}=${LinkHeader.Rel.Next}")
          )
          .filterIsLink()
      )
      .single()
      .hasPropertiesEqualToThoseOf(linkHeader)
      .prop("rel") { it.parameter(LinkHeader.Parameters.Rel) }
      .isEqualTo(LinkHeader.Rel.Next)
  }

  @Test
  fun filtersIsLinkWhenRelIsSurroundedByQuotes() {
    assertThat(
        headersOf(
            HttpHeaders.Authorization to listOf("Bearer 123"),
            HttpHeaders.Link to
              listOf("<${linkHeader.uri}>; ${LinkHeader.Parameters.Rel}=\"${LinkHeader.Rel.Next}\"")
          )
          .filterIsLink()
      )
      .single()
      .hasPropertiesEqualToThoseOf(linkHeader)
      .prop("rel") { it.parameter(LinkHeader.Parameters.Rel) }
      .isEqualTo(LinkHeader.Rel.Next)
  }
}
