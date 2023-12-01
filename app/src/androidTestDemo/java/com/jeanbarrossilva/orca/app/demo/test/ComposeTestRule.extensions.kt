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

package com.jeanbarrossilva.orca.app.demo.test

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.headline.HeadlineCard
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import kotlin.properties.Delegates
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

/** Scrolls to the first [PostPreview] containing a [HeadlineCard]. */
internal fun ComposeTestRule.performScrollToPostPreviewWithHeadlineCard() {
  var index by Delegates.notNull<Int>()
  runTest {
    index =
      Instance.sample.feedProvider
        .provide(Actor.Authenticated.sample.id, page = 0)
        .first()
        .withIndex()
        .first { it.value.content.highlight != null }
        .index
  }
  onTimeline().performScrollToIndex(index)
}

/** Performs a click on the portion located at [Offset.Zero] of this [SemanticsNode]. */
internal fun SemanticsNodeInteraction.performStartClick() {
  performTouchInput { click(Offset.Zero) }
}
