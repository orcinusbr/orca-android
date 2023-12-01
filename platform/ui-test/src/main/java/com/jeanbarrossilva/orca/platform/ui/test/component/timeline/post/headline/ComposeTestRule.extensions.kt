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

package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.headline

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.headline.HEADLINE_CARD_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.headline.HeadlineCard

/** [SemanticsNodeInteraction] of a [HeadlineCard]. */
fun ComposeTestRule.onHeadlineCard(): SemanticsNodeInteraction {
  return onNodeWithTag(HEADLINE_CARD_TAG)
}

/** [SemanticsNodeInteractionCollection] of [HeadlineCard]s. */
fun ComposeTestRule.onHeadlineCards(): SemanticsNodeInteractionCollection {
  return onAllNodesWithTag(HEADLINE_CARD_TAG)
}
