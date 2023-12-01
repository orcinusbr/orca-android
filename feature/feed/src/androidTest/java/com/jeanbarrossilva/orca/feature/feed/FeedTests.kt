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

package com.jeanbarrossilva.orca.feature.feed

import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.stat.POST_PREVIEW_FAVORITE_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.onPostPreviews
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time.Time4JTestRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class FeedTests {
  @get:Rule val time4JRule = Time4JTestRule()

  @get:Rule val composeRule = createComposeRule()

  @Test
  fun runsCallbackWhenClickingPostFavoriteStat() {
    var hasCallbackBeenRun = false
    composeRule.setContent {
      AutosTheme {
        Feed(
          PostPreview.samples.toSerializableList().toListLoadable(),
          onFavorite = { hasCallbackBeenRun = true }
        )
      }
    }
    composeRule
      .onPostPreviews()
      .onFirst()
      .onChildren()
      .filterToOne(hasTestTag(POST_PREVIEW_FAVORITE_STAT_TAG))
      .performScrollTo()
      .performClick()
    assertTrue(hasCallbackBeenRun)
  }
}
