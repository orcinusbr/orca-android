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

package com.jeanbarrossilva.orca.core.feed.profile.post.content

import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight.sample
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import com.jeanbarrossilva.orca.std.styledstring.buildStyledString
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ContentTests {
  @Test
  fun `GIVEN a text with a trailing link and a headline WHEN creating content from them THEN the link is removed`() {
    assertEquals(
      StyledString("😗"),
      Content.from(
          Domain.sample,
          buildStyledString {
            +"😗 "
            link(Highlight.sample.url) { +"🔗" }
          }
        ) {
          Headline.sample
        }
        .text
    )
  }

  @Test
  fun `GIVEN a text with a trailing link and no headline WHEN creating content from them THEN the link is kept`() {
    assertEquals(
      buildStyledString {
        link(Highlight.sample.url) { +"Link" }
        +'!'
      },
      Content.from(
          Domain.sample,
          buildStyledString {
            link(Highlight.sample.url) { +"Link" }
            +'!'
          }
        ) {
          null
        }
        .text
    )
  }

  @Test
  fun `GIVEN a text with two trailing URLs WHEN creating content from it THEN they're kept`() {
    assertEquals(
      buildStyledString { +"🫨 ${Highlight.sample.url} ${Highlight.sample.url}" },
      Content.from(
          Domain.sample,
          buildStyledString { +"🫨 ${Highlight.sample.url} ${Highlight.sample.url}" }
        ) {
          Headline.sample
        }
        .text
    )
  }

  @Test
  fun `GIVEN a text with a link to an internal resource and no headline WHEN creating content from them THEN it doesn't throw`() {
    Content.from(
      Domain.sample,
      buildStyledString {
        link(URL(Domain.sample.url, "resource")) { +"Here" }
        +'!'
      }
    ) {
      null
    }
  }
}
