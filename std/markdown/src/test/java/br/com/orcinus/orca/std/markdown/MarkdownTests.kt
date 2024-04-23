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

import br.com.orcinus.orca.std.markdown.style.Style
import br.com.orcinus.orca.std.markdown.style.type.Bold
import br.com.orcinus.orca.std.markdown.style.type.Italic
import br.com.orcinus.orca.std.markdown.style.type.Mention
import br.com.orcinus.orca.std.markdown.style.type.test.url
import br.com.orcinus.orca.std.markdown.style.type.test.username
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class MarkdownTests {
  @Test
  fun doesNotStylizeMalformattedEmailWhenItIsAppended() {
    assertContentEquals(buildMarkdown { +"john@@appleseed.com" }.styles, emptyList())
  }

  @Test
  fun throwsWhenHashtagWithMalformattedSubjectIsAppended() {
    assertFailsWith<Style.Constrained.InvalidTargetException> {
      buildMarkdown { hashtag { +"subjects - cannot - have - whitespaces" } }
    }
  }

  @Test
  fun placesAppendedMentionCorrectly() {
    assertEquals(
      Mention(indices = 7..(7 + Mention.username.lastIndex), Mention.url),
      buildMarkdown {
          +"Hello, "
          mention(Mention.url) { +Mention.username }
          +"!"
        }
        .styles
        .single()
    )
  }

  @Test
  fun nestsStyles() {
    assertContentEquals(
      listOf(Bold(0..4), Italic(0..4)),
      buildMarkdown {
          bold { italic { +"Hello" } }
          +'!'
        }
        .styles
    )
  }

  @Test
  fun chopsStylesWhenCopying() {
    assertContentEquals(
      listOf(Bold(0..4), Italic(6..7)),
      buildMarkdown {
          bold { +"Lorem" }
          +" "
          italic { +"ipsum" }
        }
        .copy { take(8) }
        .styles
    )
  }

  @Test
  fun dropsStylesWhenCopying() {
    assertContentEquals(
      listOf(Bold(0..4)),
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
