/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.input.option

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.test.onOption
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class OptionTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun isSemanticallySelectedWhenSelected() {
    composeRule.setContent { AutosTheme { Option(isSelected = true) } }
    composeRule.onOption().assertIsSelected()
  }

  @Test
  fun showsSelectionIconWhenSelected() {
    composeRule.setContent { AutosTheme { Option(isSelected = true) } }
    composeRule.onNodeWithTag(OPTION_SELECTION_ICON_TAG, useUnmergedTree = true).assertIsDisplayed()
  }

  @Test
  fun runsCallbackWhenSelected() {
    var isSelected by mutableStateOf(false)
    composeRule.setContent {
      AutosTheme { Option(isSelected, onSelectionToggle = { isSelected = it }) }
    }
    composeRule.onOption().performClick()
    assertTrue(isSelected)
  }

  @Test
  fun runsCallbackWhenUnselected() {
    var isSelected by mutableStateOf(true)
    composeRule.setContent {
      AutosTheme { Option(isSelected, onSelectionToggle = { isSelected = it }) }
    }
    composeRule.onOption().performClick()
    assertFalse(isSelected)
  }
}
