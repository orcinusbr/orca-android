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

package com.jeanbarrossilva.orca.platform.autos.kit.input.option.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.OPTION_TAG
import com.jeanbarrossilva.orca.platform.autos.kit.input.option.Option

/** [SemanticsNodeInteraction] of an [Option]. */
internal fun ComposeTestRule.onOption(): SemanticsNodeInteraction {
  return onNodeWithTag(OPTION_TAG)
}

/** [SemanticsNodeInteractionCollection] of [Option]s. */
internal fun ComposeTestRule.onOptions(): SemanticsNodeInteractionCollection {
  return onAllNodesWithTag(OPTION_TAG)
}
