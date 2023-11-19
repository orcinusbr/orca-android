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
