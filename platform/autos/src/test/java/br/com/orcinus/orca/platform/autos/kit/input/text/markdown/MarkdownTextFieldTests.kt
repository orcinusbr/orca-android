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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTextInput
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.state.MarkdownTextFieldStateTestRule
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import br.com.orcinus.orca.std.markdown.style.Style
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MarkdownTextFieldTests {
  @get:Rule val stateRule = MarkdownTextFieldStateTestRule()
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun doesNotChangeText() {
    composeRule
      .apply {
        setContent {
          AutosTheme {
            MarkdownTextField(stateRule.state, MarkdownTextFieldValue.Empty, onValueChange = {})
          }
        }
      }
      .onMarkdownTextField()
      .apply { performTextInput(":c") }
      .assertTextEquals("")
  }

  @Test
  fun notifiesChangesToValue() {
    var value by mutableStateOf(MarkdownTextFieldValue.Empty)
    composeRule
      .apply {
        setContent {
          AutosTheme { MarkdownTextField(stateRule.state, value, onValueChange = { value = it }) }
        }
      }
      .onMarkdownTextField()
      .performTextInput(":o")
    assertThat(value).isEqualTo(MarkdownTextFieldValue(Markdown.unstyled(":o"), selection = 2..2))
  }

  @Test
  fun notifiesChangeToTextWhenStyleIsApplied() {
    var value by mutableStateOf(MarkdownTextFieldValue(Markdown.unstyled("君の名は。")))
    composeRule.setContent {
      AutosTheme { MarkdownTextField(stateRule.state, value, onValueChange = { value = it }) }
    }
    stateRule.state.toggle(Style.Bold(indices = 0..0))
    assertThat(value.text.styles)
      .isEqualTo(
        buildMarkdown {
            bold { +'君' }
            +"の名は。"
          }
          .styles
      )
  }
}
