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

package br.com.orcinus.orca.core.feed.profile.post.content

import assertk.assertThat
import assertk.assertions.isSameAs
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Headline
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Highlight
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.core.sample.test.feed.profile.post.content.highlight.sample
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ContentTests {
  @Test
  fun returnsTheEmptyContentWhenCreatingOneWithAnEmptyTextAndWithoutAttachments() {
    assertThat(Content.empty).isSameAs(Content.empty)
  }

  @Test
  fun `GIVEN a text with a trailing link and a headline WHEN creating content from them THEN the link is removed`() {
    assertEquals(
      Markdown.unstyled("😗"),
      Content.from(
          Domain.sample,
          buildMarkdown {
            +"😗 "
            link(Highlight.sample.uri) { +"🔗" }
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
      buildMarkdown {
        link(Highlight.sample.uri) { +"Link" }
        +'!'
      },
      Content.from(
          Domain.sample,
          buildMarkdown {
            link(Highlight.sample.uri) { +"Link" }
            +'!'
          }
        ) {
          null
        }
        .text
    )
  }

  @Test
  fun `GIVEN a text with two trailing URIs WHEN creating content from it THEN they're kept`() {
    assertEquals(
      buildMarkdown { +"🫨 ${Highlight.sample.uri} ${Highlight.sample.uri}" },
      Content.from(
          Domain.sample,
          buildMarkdown { +"🫨 ${Highlight.sample.uri} ${Highlight.sample.uri}" }
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
      buildMarkdown {
        link(HostedURLBuilder.from(Domain.sample.uri).path("resource").build()) { +"Here" }
        +'!'
      }
    ) {
      null
    }
  }
}
