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

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.performScrollToNode
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

/**
 * Scrolls to the bottom of the [Timeline] to which the [SemanticsNode] refers to.
 *
 * @throws AssertionError If the [SemanticsNode] isn't that of a [Timeline].
 */
@Throws(AssertionError::class)
fun SemanticsNodeInteraction.performScrollToBottom(): SemanticsNodeInteraction {
  assert(isTimeline()) { "Can only scroll to the bottom of a Timeline." }
  return performScrollToNode(isRenderEffect())
}
