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

package br.com.orcinus.orca.composite.timeline.text.annotated.span

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.composite.timeline.text.annotated.toStyledString
import br.com.orcinus.orca.std.styledstring.StyledString
import br.com.orcinus.orca.std.styledstring.buildStyledString
import kotlin.test.Test

internal class AnnotatedStringExtensionsTests {
  @Test
  fun convertsAnnotatedStringWithoutAnnotationsIntoStyledString() {
    assertThat(AnnotatedString("Hello, world!").toStyledString())
      .isEqualTo(StyledString("Hello, world!"))
  }

  @Test
  fun convertsEmboldenedAnnotatedStringIntoStyledString() {
    assertThat(
        buildAnnotatedString {
            withStyle(BoldSpanStyle) { append("Hello") }
            append(", world!")
          }
          .toStyledString()
      )
      .isEqualTo(
        buildStyledString {
          bold { +"Hello" }
          +", world!"
        }
      )
  }

  @Test
  fun convertsItalicizedAnnotatedStringIntoStyledString() {
    assertThat(
        buildAnnotatedString {
            withStyle(ItalicSpanStyle) { append("Hello") }
            append(", world!")
          }
          .toStyledString()
      )
      .isEqualTo(
        buildStyledString {
          italic { +"Hello" }
          +", world!"
        }
      )
  }
}
