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

package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview

/** [SemanticsNodeInteractionCollection] of [PostPreview] nodes. */
fun ComposeTestRule.onPostPreviews(): SemanticsNodeInteractionCollection {
  return onAllNodes(isPostPreview())
}

/** [SemanticsNodeInteraction] of a [PostPreview] node. */
fun ComposeTestRule.onPostPreview(): SemanticsNodeInteraction {
  return onNode(isPostPreview())
}
