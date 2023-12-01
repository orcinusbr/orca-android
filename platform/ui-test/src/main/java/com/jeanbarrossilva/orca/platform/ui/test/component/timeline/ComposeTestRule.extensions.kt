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

package com.jeanbarrossilva.orca.platform.ui.test.component.timeline

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.ui.component.timeline.TIMELINE_REFRESH_INDICATOR
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

/** [SemanticsNodeInteraction] of a [Timeline]'s refresh indicator. */
fun ComposeTestRule.onRefreshIndicator(): SemanticsNodeInteraction {
  return onNodeWithTag(TIMELINE_REFRESH_INDICATOR)
}

/** [SemanticsNodeInteraction] of a [Timeline] node. */
fun ComposeTestRule.onTimeline(): SemanticsNodeInteraction {
  return onNode(isTimeline())
}
