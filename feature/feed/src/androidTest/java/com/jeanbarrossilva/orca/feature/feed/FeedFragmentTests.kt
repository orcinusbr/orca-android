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

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.samples
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.feature.feed.test.FeedActivity
import com.jeanbarrossilva.orca.feature.feed.test.TestFeedModule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.stat.POST_PREVIEW_FAVORITE_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.performScrollToBottom
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.isPostPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.onPostPreviews
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time.Time4JTestRule
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class FeedFragmentTests {
  @get:Rule val injectorRule = InjectorTestRule { register<FeedModule>(TestFeedModule) }
  @get:Rule val sampleInstanceRule = SampleInstanceTestRule()
  @get:Rule val time4JRule = Time4JTestRule()
  @get:Rule val composeRule = createEmptyComposeRule()

  @Test
  fun keepsPreviousPostsWhenLoadingNextOnes() {
    launchActivity<FeedActivity>(FeedActivity.getIntent(Profile.sample.id)).use {
      composeRule
        .onTimeline()
        .performScrollToBottom()
        .onChildren()
        .filter(isPostPreview())
        .assertCountEquals(Post.samples.size)
    }
  }

  @Test
  fun favoritesPost() {
    runTest {
      Instance.sample.feedProvider
        .provide(Actor.Authenticated.sample.id, page = 0)
        .first()
        .first()
        .favorite
        .disable()
    }
    launchActivity<FeedActivity>(FeedActivity.getIntent(Profile.sample.id)).use {
      composeRule
        .onPostPreviews()
        .onFirst()
        .onChildren()
        .filterToOne(hasTestTag(POST_PREVIEW_FAVORITE_STAT_TAG))
        .performScrollTo()
        .performClick()
        .assertIsSelected()
    }
  }
}
