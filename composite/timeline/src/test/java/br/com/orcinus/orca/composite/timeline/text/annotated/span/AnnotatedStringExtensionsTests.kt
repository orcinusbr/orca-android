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
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.toMarkdown
import br.com.orcinus.orca.platform.markdown.annotated.BoldSpanStyle
import br.com.orcinus.orca.platform.markdown.annotated.ItalicSpanStyle
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import kotlin.test.Test

internal class AnnotatedStringExtensionsTests {
  @Test
  fun convertsAnnotatedStringWithoutAnnotationsIntoMarkdown() {
    assertThat(AnnotatedString("Hello, world!").toMarkdown())
      .isEqualTo(Markdown.unstyled("Hello, world!"))
  }

  @Test
  fun convertsEmboldenedAnnotatedStringIntoMarkdown() {
    assertThat(
        buildAnnotatedString {
            withStyle(BoldSpanStyle) { append("Hello") }
            append(", world!")
          }
          .toMarkdown()
      )
      .isEqualTo(
        buildMarkdown {
          bold { +"Hello" }
          +", world!"
        }
      )
  }

  @Test
  fun convertsItalicizedAnnotatedStringIntoMarkdown() {
    assertThat(
        buildAnnotatedString {
            withStyle(ItalicSpanStyle) { append("Hello") }
            append(", world!")
          }
          .toMarkdown()
      )
      .isEqualTo(
        buildMarkdown {
          italic { +"Hello" }
          +", world!"
        }
      )
  }
}
