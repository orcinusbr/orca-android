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

package br.com.orcinus.orca.std.markdown

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import br.com.orcinus.orca.std.markdown.style.Style
import java.net.URI
import kotlin.test.Test
import kotlin.test.assertContentEquals

internal class MarkdownTests {
  @Test
  fun doesNotStylizeMalformattedEmailWhenItIsAppended() {
    assertThat(buildMarkdown { +"john@@appleseed.com" }.styles).isEmpty()
  }

  @Test
  fun stylizesAppendedEmail() {
    assertThat(buildMarkdown { +"jean@orcinus.com.br" }.styles)
      .containsExactly(Style.Link(URI("mailto", "jean@orcinus.com.br", null), indices = 0..18))
  }

  @Test
  fun nestsStyles() {
    assertThat(
        buildMarkdown {
            bold { italic { +"Hello" } }
            +'!'
          }
          .styles
      )
      .containsExactly(Style.Bold(0..4), Style.Italic(0..4))
  }

  @Test
  fun chopsStylesWhenCopying() {
    assertThat(
        buildMarkdown {
            bold { +"Lorem" }
            +" "
            italic { +"ipsum" }
          }
          .copy { take(8) }
          .styles
      )
      .containsExactly(Style.Bold(0..4), Style.Italic(6..7))
  }

  @Test
  fun dropsStylesWhenCopying() {
    assertContentEquals(
      listOf(Style.Bold(0..4)),
      buildMarkdown {
          bold { +"Lorem" }
          +" "
          italic { +"ipsum" }
        }
        .copy { take(5) }
        .styles
    )
  }
}
