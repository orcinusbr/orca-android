package com.jeanbarrossilva.orca.platform.theme.kit.input.option

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.test.onOption
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class OptionTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun isSemanticallySelectedWhenSelected() {
        composeRule.setContent {
            OrcaTheme {
                Option(isSelected = true)
            }
        }
        composeRule.onOption().assertIsSelected()
    }

    @Test
    fun showsSelectionIconWhenSelected() {
        composeRule.setContent {
            OrcaTheme {
                Option(isSelected = true)
            }
        }
        composeRule
            .onNodeWithTag(OPTION_SELECTION_ICON_TAG, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun runsCallbackWhenSelected() {
        var isSelected by mutableStateOf(false)
        composeRule.setContent {
            OrcaTheme {
                Option(isSelected, onSelectionToggle = { isSelected = it })
            }
        }
        composeRule.onOption().performClick()
        assertTrue(isSelected)
    }

    @Test
    fun runsCallbackWhenUnselected() {
        var isSelected by mutableStateOf(true)
        composeRule.setContent {
            OrcaTheme {
                Option(isSelected, onSelectionToggle = { isSelected = it })
            }
        }
        composeRule.onOption().performClick()
        assertFalse(isSelected)
    }
}
