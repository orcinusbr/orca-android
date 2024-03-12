/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.test.fillScreenSize
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import org.junit.Rule
import org.junit.Test

internal class ButtonBarTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun hidesDividerWhenListCannotBeScrolled() {
    val lazyListState = LazyListState()
    composeRule.setContent {
      AutosTheme {
        Scaffold(bottom = { ButtonBar(lazyListState) }) {
          expanded { LazyColumn(state = lazyListState) { item { Spacer(Modifier) } } }
        }
      }
    }
    composeRule.onNodeWithTag(BUTTON_BAR_DIVIDER_TAG).assertIsNotDisplayed()
  }

  @Test
  fun showsDividerWhenListCanBeScrolled() {
    val lazyListState = LazyListState()
    composeRule.setContent {
      AutosTheme {
        Scaffold(bottom = { ButtonBar(lazyListState) }) {
          expanded {
            LazyColumn(state = lazyListState) {
              item { Spacer(Modifier.padding(it).fillScreenSize()) }
            }
          }
        }
      }
    }
    composeRule.onNodeWithTag(BUTTON_BAR_DIVIDER_TAG).assertIsDisplayed()
  }
}
