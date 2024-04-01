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
