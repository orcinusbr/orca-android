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

package com.jeanbarrossilva.orca.feature.settings.termmuting

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.feature.settings.termmuting.test.TestTermMuting
import com.jeanbarrossilva.orca.platform.autos.test.kit.input.text.onTextFieldErrors
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import org.junit.Rule
import org.junit.Test

internal class TermMutingTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsErrorWhenTermIsEmptyAndMuteButtonIsClicked() {
    composeRule.setContent { AutosTheme { TestTermMuting() } }
    composeRule.onNodeWithTag(SETTINGS_TERM_MUTING_MUTE_BUTTON).performClick()
    composeRule.onTextFieldErrors().assertIsDisplayed()
  }
}
