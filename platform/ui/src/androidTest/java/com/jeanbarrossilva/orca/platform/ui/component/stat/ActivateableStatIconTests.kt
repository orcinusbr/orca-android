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

package com.jeanbarrossilva.orca.platform.ui.component.stat

import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.test.onActivateableStatIcon
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class ActivateableStatIconTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun isUnselectedWhenInactive() {
    composeRule.setContent { AutosTheme { TestActivateableStatIcon(isActive = false) } }
    composeRule.onActivateableStatIcon().assertIsNotSelected()
  }

  @Test
  fun isSelectedWhenActive() {
    composeRule.setContent { AutosTheme { TestActivateableStatIcon(isActive = true) } }
    composeRule.onActivateableStatIcon().assertIsSelected()
  }

  @Test
  fun receivesInteractionWhenInteractive() {
    var hasBeenInteractedWith = false
    composeRule.setContent {
      AutosTheme {
        TestActivateableStatIcon(
          interactiveness =
            ActivateableStatIconInteractiveness.Interactive { hasBeenInteractedWith = true }
        )
      }
    }
    composeRule.onActivateableStatIcon().performClick()
    assertTrue(hasBeenInteractedWith)
  }
}
