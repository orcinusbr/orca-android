package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.test.fillScreenSize
import org.junit.Rule
import org.junit.Test

internal class ButtonBarTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun hidesDividerWhenListCannotBeScrolled() {
    val lazyListState = LazyListState()
    composeRule.setContent {
      OrcaTheme {
        Scaffold(buttonBar = { ButtonBar(lazyListState) }) {
          LazyColumn(state = lazyListState) { item { Spacer(Modifier) } }
        }
      }
    }
    composeRule.onNodeWithTag(BUTTON_BAR_DIVIDER_TAG).assertIsNotDisplayed()
  }

  @Test
  fun showsDividerWhenListCanBeScrolled() {
    val lazyListState = LazyListState()
    composeRule.setContent {
      OrcaTheme {
        Scaffold(buttonBar = { ButtonBar(lazyListState) }) {
          LazyColumn(state = lazyListState) {
            item { Spacer(Modifier.padding(it).fillScreenSize()) }
          }
        }
      }
    }
    composeRule.onNodeWithTag(BUTTON_BAR_DIVIDER_TAG).assertIsDisplayed()
  }
}
