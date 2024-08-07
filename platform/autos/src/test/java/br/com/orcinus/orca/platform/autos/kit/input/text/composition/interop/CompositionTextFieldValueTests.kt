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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isSameAs
import assertk.assertions.isTrue
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import br.com.orcinus.orca.std.markdown.style.Style
import kotlin.test.Test

internal class CompositionTextFieldValueTests {
  @Test
  fun styleIsSelected() {
    assertThat(
        CompositionTextFieldValue(
            buildMarkdown {
              bold { +"Hello" }
              +'!'
            },
            selection = 0..4
          )
          .isSelected<Style.Bold>()
      )
      .isTrue()
  }

  @Test
  fun styleIsPartiallySelected() {
    assertThat(
        CompositionTextFieldValue(
            buildMarkdown {
              bold { +"Hello" }
              +'!'
            },
            selection = 2..3
          )
          .isSelected<Style.Bold>()
      )
      .isTrue()
  }

  @Test
  fun styleIsNotSelected() {
    assertThat(
        CompositionTextFieldValue(
            buildMarkdown {
              bold { +"Hello" }
              +'!'
            },
            selection = 5..5
          )
          .isSelected<Style.Bold>()
      )
      .isFalse()
  }

  @Test
  fun addsStyle() {
    assertThat(
        CompositionTextFieldValue(
            buildMarkdown {
              +"Hello"
              italic { +'!' }
            },
            selection = 0..5
          )
          .toggle(Style::Bold)
      )
      .isEqualTo(
        CompositionTextFieldValue(
          buildMarkdown {
            bold { +"Hello" }
            italic { +'!' }
          },
          selection = 0..5
        )
      )
  }

  @Test
  fun removesStyle() {
    assertThat(
        CompositionTextFieldValue(
            buildMarkdown {
              bold { +"Hello" }
              italic { +'!' }
            },
            selection = 0..5
          )
          .toggle(Style::Bold)
      )
      .isEqualTo(
        CompositionTextFieldValue(
          buildMarkdown {
            +"Hello"
            italic { +'!' }
          },
          selection = 0..5
        )
      )
  }

  @Test
  fun orEmptyReturnsReceiverValueWhenItIsNotNull() {
    val value = CompositionTextFieldValue(Markdown.unstyled("Hello, world!"))
    assertThat(value.orEmpty()).isSameAs(value)
  }

  @Test
  fun orEmptyReturnsEmptyValueWhenReceiverIsNull() {
    assertThat((null as CompositionTextFieldValue?).orEmpty())
      .isSameAs(CompositionTextFieldValue.Empty)
  }
}
