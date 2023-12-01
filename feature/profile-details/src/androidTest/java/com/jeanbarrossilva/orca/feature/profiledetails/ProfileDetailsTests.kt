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

package com.jeanbarrossilva.orca.feature.profiledetails

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.TIMELINE_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time.TestRelativeTimeProvider
import java.util.UUID
import org.junit.Rule
import org.junit.Test

internal class ProfileDetailsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsUsernameWhenScrollingPastHeader() {
    val screenHeightInPx =
      InstrumentationRegistry.getInstrumentation().context.resources.displayMetrics.heightPixels
    composeRule.setContent {
      AutosTheme {
        ProfileDetails(
          postPreviewsLoadable =
            List(size = screenHeightInPx) { PostPreview.sample.copy(id = "${UUID.randomUUID()}") }
              .toSerializableList()
              .toListLoadable(),
          relativeTimeProvider = TestRelativeTimeProvider
        )
      }
    }
    composeRule.onTimeline().performScrollToIndex(1)
    composeRule.onNodeWithTag(TIMELINE_TAG).assertIsDisplayed()
  }
}
