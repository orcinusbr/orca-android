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

package br.com.orcinus.orca.platform.markdown

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTextInput
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.platform.markdown.state.MarkdownTextFieldStateTestRule
import br.com.orcinus.orca.platform.markdown.test.onMarkdownTextField
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import br.com.orcinus.orca.std.markdown.style.Style
import kotlin.test.Ignore
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MarkdownTextFieldTests {
  @get:Rule val stateRule = MarkdownTextFieldStateTestRule()
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun notifiesChangesToText() {
    var text by mutableStateOf(Markdown(""))
    composeRule
      .apply {
        setContent { MarkdownTextField(stateRule.state, text, onTextChange = { text = it }) }
      }
      .onMarkdownTextField()
      .performTextInput(":o")
    assertThat(text).isEqualTo(":o")
  }

  @Ignore
  @Test
  fun notifiesChangeToTextWhenStyleIsApplied() {
    var text by mutableStateOf(Markdown("君の名は。"))
    composeRule.setContent {
      MarkdownTextField(stateRule.state, text, onTextChange = { text = it })
    }
    stateRule.state.toggle(Style.Bold(indices = 0..0))
    assertThat(text.styles)
      .isEqualTo(
        buildMarkdown {
            bold { +'君' }
            +"の名は。"
          }
          .styles
      )
  }
}
