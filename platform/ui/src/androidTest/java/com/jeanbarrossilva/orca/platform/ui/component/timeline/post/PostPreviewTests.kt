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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onLast
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.sample
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewBody
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewCommentCountStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewFavoriteCountStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewMetadata
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewName
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewReblogCountStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewReblogMetadata
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewShareAction
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onStatLabel
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.onPostPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time.TestRelativeTimeProvider
import org.junit.Rule
import org.junit.Test

internal class PostPreviewTests {
  @get:Rule val coreSampleRule = SampleInstanceTestRule()

  @get:Rule val composeRule = createComposeRule()

  private val samplePostPreview
    get() = PostPreview.getSample(context, AutosTheme.getColors(context))

  private val context
    get() = InstrumentationRegistry.getInstrumentation().context

  @Test
  fun isShownWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onPostPreview().assertIsDisplayed()
  }

  @Test
  fun isShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onPostPreview().assertIsDisplayed()
  }

  @Test
  fun nameIsLoadingWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onPostPreviewName().assertIsLoading()
  }

  @Test
  fun nameIsShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onPostPreviewName().assertIsNotLoading()
    composeRule.onPostPreviewName().assertTextEquals(samplePostPreview.name)
  }

  @Test
  fun metadataIsLoadingWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onPostPreviewMetadata().assertIsLoading()
  }

  @Test
  fun metadataIsShownWhenLoaded() {
    val relativeTimeProvider = TestRelativeTimeProvider
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = relativeTimeProvider) }
    }
    composeRule.onPostPreviewMetadata().assertIsNotLoading()
    composeRule
      .onPostPreviewMetadata()
      .assertTextEquals(samplePostPreview.getMetadata(relativeTimeProvider))
  }

  @Test
  fun reblogMetadataIsNotShownWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onPostPreviewReblogMetadata().assertDoesNotExist()
  }

  @Test
  fun reblogMetadataIndicatesThatPostHasBeenRebloggedBySomeone() {
    composeRule.setContent {
      AutosTheme {
        SamplePostPreview(
          preview = PostPreview.sample.copy(rebloggerName = Author.sample.name),
          relativeTimeProvider = TestRelativeTimeProvider
        )
      }
    }
    composeRule
      .onPostPreviewReblogMetadata()
      .onChildren()
      .onLast()
      .assertTextEquals(
        context.getString(R.string.platform_ui_post_preview_reposted, Author.sample.name)
      )
  }

  @Test
  fun bodyIsLoadingWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onPostPreviewBody().assertIsLoading()
  }

  @Test
  fun bodyIsShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onPostPreviewBody().assertIsNotLoading()
    composeRule.onPostPreviewBody().assertTextEquals(samplePostPreview.text.text)
  }

  @Test
  fun commentCountStatIsNonexistentWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onPostPreviewCommentCountStat().assertDoesNotExist()
  }

  @Test
  fun commentCountStatIsShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onPostPreviewCommentCountStat().assertIsDisplayed()
    composeRule
      .onPostPreviewCommentCountStat()
      .onStatLabel()
      .assertTextEquals(samplePostPreview.formattedCommentCount)
  }

  @Test
  fun favoriteCountStatIsNonexistentWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onPostPreviewFavoriteCountStat().assertDoesNotExist()
  }

  @Test
  fun favoriteCountStatIsShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onPostPreviewFavoriteCountStat().assertIsDisplayed()
    composeRule
      .onPostPreviewFavoriteCountStat()
      .onStatLabel()
      .assertTextEquals(samplePostPreview.formattedFavoriteCount)
  }

  @Test
  fun reblogCountStatIsNonexistentWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onPostPreviewReblogCountStat().assertDoesNotExist()
  }

  @Test
  fun reblogCountStatIsShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onPostPreviewReblogCountStat().assertIsDisplayed()
    composeRule
      .onPostPreviewReblogCountStat()
      .onStatLabel()
      .assertTextEquals(samplePostPreview.formattedReblogCount)
  }

  @Test
  fun shareActionIsNonexistentWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onPostPreviewShareAction().assertDoesNotExist()
  }

  @Test
  fun shareActionIsShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onPostPreviewShareAction().assertIsDisplayed()
  }
}
