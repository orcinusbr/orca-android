/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
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
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.core.sample
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.avatar.sample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewBody
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewMetadata
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewName
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onPostPreviewReblogMetadata
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test.onStatLabel
import com.jeanbarrossilva.orca.platform.ui.test.component.stat.favorite.onFavoriteStat
import com.jeanbarrossilva.orca.platform.ui.test.component.stat.onCommentStat
import com.jeanbarrossilva.orca.platform.ui.test.component.stat.onShareStat
import com.jeanbarrossilva.orca.platform.ui.test.component.stat.repost.onRepostStat
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.onPostPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time.TestRelativeTimeProvider
import org.junit.Rule
import org.junit.Test

internal class PostPreviewTests {
  @get:Rule val coreSampleRule = SampleInstanceTestRule(Instance.sample)

  @get:Rule val composeRule = createComposeRule()

  private val samplePostPreview
    get() = PostPreview.getSample(AutosTheme.getColors(context))

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
    composeRule.onCommentStat().assertDoesNotExist()
  }

  @Test
  fun commentCountStatIsShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onCommentStat().assertIsDisplayed()
    composeRule
      .onCommentStat()
      .onStatLabel()
      .assertTextEquals(samplePostPreview.stats.formattedCommentCount)
  }

  @Test
  fun favoriteCountStatIsNonexistentWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onFavoriteStat().assertDoesNotExist()
  }

  @Test
  fun favoriteCountStatIsShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onFavoriteStat().assertIsDisplayed()
    composeRule
      .onFavoriteStat()
      .onStatLabel()
      .assertTextEquals(samplePostPreview.stats.formattedFavoriteCount)
  }

  @Test
  fun reblogCountStatIsNonexistentWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onRepostStat().assertDoesNotExist()
  }

  @Test
  fun reblogCountStatIsShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onRepostStat().assertIsDisplayed()
    composeRule
      .onRepostStat()
      .onStatLabel()
      .assertTextEquals(samplePostPreview.stats.formattedReblogCount)
  }

  @Test
  fun shareActionIsNonexistentWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onShareStat().assertDoesNotExist()
  }

  @Test
  fun shareActionIsShownWhenLoaded() {
    composeRule.setContent {
      AutosTheme { SamplePostPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onShareStat().assertIsDisplayed()
  }
}
