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

package com.jeanbarrossilva.orca.feature.composer.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.feature.composer.COMPOSER_FIELD
import com.jeanbarrossilva.orca.feature.composer.Composer
import com.jeanbarrossilva.orca.feature.composer.ui.COMPOSER_TOOLBAR
import com.jeanbarrossilva.orca.feature.composer.ui.Toolbar

/** [SemanticsNodeInteraction] of a [Composer]'s field. */
internal fun ComposeTestRule.onField(): SemanticsNodeInteraction {
  return onNodeWithTag(COMPOSER_FIELD)
}

/** [SemanticsNodeInteraction] of a [Toolbar]. */
internal fun ComposeTestRule.onToolbar(): SemanticsNodeInteraction {
  return onNodeWithTag(COMPOSER_TOOLBAR)
}
