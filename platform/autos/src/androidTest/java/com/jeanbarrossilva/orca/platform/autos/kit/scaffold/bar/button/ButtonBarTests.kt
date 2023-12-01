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
      AutosTheme {
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
