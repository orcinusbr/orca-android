/*
 * Copyright © 2023-2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.composite.timeline.post

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onLast
import androidx.test.platform.app.InstrumentationRegistry
import br.com.orcinus.orca.composite.timeline.R
import br.com.orcinus.orca.composite.timeline.avatar.sample
import br.com.orcinus.orca.composite.timeline.post.test.onPostPreviewBody
import br.com.orcinus.orca.composite.timeline.post.test.onPostPreviewMetadata
import br.com.orcinus.orca.composite.timeline.post.test.onPostPreviewName
import br.com.orcinus.orca.composite.timeline.post.test.onPostPreviewRepostMetadata
import br.com.orcinus.orca.composite.timeline.post.test.onStatLabel
import br.com.orcinus.orca.composite.timeline.test.post.onPostPreview
import br.com.orcinus.orca.composite.timeline.test.post.time.StringRelativeTimeProvider
import br.com.orcinus.orca.composite.timeline.test.stat.activateable.favorite.onFavoriteStat
import br.com.orcinus.orca.composite.timeline.test.stat.activateable.repost.onRepostStat
import br.com.orcinus.orca.composite.timeline.test.stat.onCommentStat
import br.com.orcinus.orca.composite.timeline.test.stat.onShareStat
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.test.instance.SampleInstanceTestRule
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.core.sample
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
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
      AutosTheme { LoadedPostPreview(relativeTimeProvider = StringRelativeTimeProvider) }
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
      AutosTheme { LoadedPostPreview(relativeTimeProvider = StringRelativeTimeProvider) }
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
    composeRule.setContent {
      AutosTheme { LoadedPostPreview(relativeTimeProvider = StringRelativeTimeProvider) }
    }
    composeRule.onPostPreviewMetadata().assertIsNotLoading()
    composeRule
      .onPostPreviewMetadata()
      .assertTextEquals(samplePostPreview.getMetadata(StringRelativeTimeProvider))
  }

  @Test
  fun repostMetadataIsNotShownWhenLoading() {
    composeRule.setContent { AutosTheme { PostPreview() } }
    composeRule.onPostPreviewRepostMetadata().assertDoesNotExist()
  }

  @Test
  fun reblogMetadataIndicatesThatPostHasBeenRebloggedBySomeone() {
    composeRule.setContent {
      AutosTheme {
        LoadedPostPreview(
          preview = PostPreview.sample.copy(rebloggerName = Author.sample.name),
          relativeTimeProvider = StringRelativeTimeProvider
        )
      }
    }
    composeRule
      .onPostPreviewRepostMetadata()
      .onChildren()
      .onLast()
      .assertTextEquals(
        context.getString(R.string.composite_timeline_post_preview_reposted, Author.sample.name)
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
      AutosTheme { LoadedPostPreview(relativeTimeProvider = StringRelativeTimeProvider) }
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
      AutosTheme { LoadedPostPreview(relativeTimeProvider = StringRelativeTimeProvider) }
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
      AutosTheme { LoadedPostPreview(relativeTimeProvider = StringRelativeTimeProvider) }
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
      AutosTheme { LoadedPostPreview(relativeTimeProvider = StringRelativeTimeProvider) }
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
      AutosTheme { LoadedPostPreview(relativeTimeProvider = StringRelativeTimeProvider) }
    }
    composeRule.onShareStat().assertIsDisplayed()
  }
}
