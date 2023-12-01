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

package com.jeanbarrossilva.orca.std.styledstring

import com.jeanbarrossilva.orca.std.styledstring.style.Style
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.style.type.Italic
import com.jeanbarrossilva.orca.std.styledstring.style.type.Mention
import com.jeanbarrossilva.orca.std.styledstring.style.type.test.url
import com.jeanbarrossilva.orca.std.styledstring.style.type.test.username
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class StyledStringTests {
  @Test
  fun `GIVEN an invalid e-mail WHEN appending it THEN it isn't stylized as an e-mail`() {
    assertContentEquals(buildStyledString { +"john@@appleseed.com" }.styles, emptyList())
  }

  @Test
  fun `GIVEN an invalid subject WHEN appending a hashtag THEN it throws`() {
    assertFailsWith<Style.Constrained.InvalidTargetException> {
      buildStyledString { hashtag { +"subjects - cannot - have - whitespaces" } }
    }
  }

  @Test
  fun `GIVEN a mention WHEN appending it THEN it's been placed correctly`() {
    assertEquals(
      Mention(indices = 7..(7 + Mention.username.lastIndex), Mention.url),
      buildStyledString {
          +"Hello, "
          mention(Mention.url) { +Mention.username }
          +"!"
        }
        .also(::println)
        .styles
        .single()
    )
  }

  @Test
  fun `GIVEN nested styles WHEN appending text with them THEN they've been applied`() {
    assertContentEquals(
      listOf(Bold(0..4), Italic(0..4)),
      buildStyledString {
          bold { italic { +"Hello" } }
          +'!'
        }
        .styles
    )
  }

  @Test
  fun `GIVEN a styled string WHEN copying it with chopped styles THEN its styles are adapted`() {
    assertContentEquals(
      listOf(Bold(0..4), Italic(6..7)),
      buildStyledString {
          bold { +"Lorem" }
          +" "
          italic { +"ipsum" }
        }
        .copy { take(8) }
        .styles
    )
  }

  @Test
  fun `GIVEN a styled string WHEN copying it with dropped styles THEN its styles are adapted`() {
    assertContentEquals(
      listOf(Bold(0..4)),
      buildStyledString {
          bold { +"Lorem" }
          +" "
          italic { +"ipsum" }
        }
        .copy { take(5) }
        .styles
    )
  }
}
