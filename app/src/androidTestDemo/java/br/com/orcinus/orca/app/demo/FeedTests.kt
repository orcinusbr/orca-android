/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.app.demo

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import assertk.assertThat
import br.com.orcinus.orca.app.demo.activity.DemoOrcaActivity
import br.com.orcinus.orca.composite.timeline.test.onTimeline
import br.com.orcinus.orca.composite.timeline.test.post.figure.gallery.thumbnail.onThumbnails
import br.com.orcinus.orca.composite.timeline.test.post.performScrollToPostPreviewWithGalleryPreview
import br.com.orcinus.orca.composite.timeline.test.search.field.isResultCard
import br.com.orcinus.orca.composite.timeline.test.search.field.onResultCard
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.feature.feed.FeedFragment
import br.com.orcinus.orca.feature.feed.test.onSearchAction
import br.com.orcinus.orca.feature.gallery.test.ui.onCloseActionButton
import br.com.orcinus.orca.feature.gallery.test.ui.onPager
import br.com.orcinus.orca.feature.gallery.test.ui.performScrollToEachPage
import br.com.orcinus.orca.platform.autos.test.kit.input.text.onSearchTextField
import br.com.orcinus.orca.platform.navigation.test.isAt
import br.com.orcinus.orca.platform.testing.compose.onEach
import org.junit.Rule
import org.junit.Test

internal class FeedTests {
  private inline val feedProvider
    get() = composeRule.activity.coreModule.instance.feedProvider

  @get:Rule val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @Test
  fun searches() {
    composeRule.onSearchAction().performClick()
    composeRule.onSearchTextField().performTextInput("${Account.sample}")
    @OptIn(ExperimentalTestApi::class) composeRule.waitUntilExactlyOneExists(isResultCard())
    composeRule.onResultCard().assertIsDisplayed()
  }

  @Test
  fun navigatesToGalleryAndGoesBackToFeedWhenClosingIt() {
    with(composeRule) {
      onTimeline().performScrollToPostPreviewWithGalleryPreview(feedProvider) {
        onThumbnails().onFirst().performClick()
        onCloseActionButton().performClick()
        assertThat(composeRule.activity).isAt<_, FeedFragment>()
      }
    }
  }

  @Test
  fun showsImageWhoseThumbnailWasClickedWhenNavigatingToGallery() {
    with(composeRule) {
      onTimeline().performScrollToPostPreviewWithGalleryPreview(feedProvider) { post ->
        onThumbnails().onEach { index ->
          performClick()
          onPager().performScrollToEachPage {
            assertContentDescriptionIsCohesiveToPagePosition(post, entrypointIndex = index)
          }
          onCloseActionButton().performClick()
        }
      }
    }
  }
}
