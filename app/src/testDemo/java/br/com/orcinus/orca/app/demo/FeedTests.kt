/*
 * Copyright © 2023–2024 Orcinus
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

import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import br.com.orcinus.orca.composite.timeline.test.onRefreshIndicator
import br.com.orcinus.orca.composite.timeline.test.onTimeline
import br.com.orcinus.orca.composite.timeline.test.post.figure.gallery.thumbnail.onThumbnails
import br.com.orcinus.orca.composite.timeline.test.post.figure.link.onLinkCards
import br.com.orcinus.orca.composite.timeline.test.post.performScrollToPostPreviewWithGalleryPreview
import br.com.orcinus.orca.composite.timeline.test.post.performScrollToPostPreviewWithLinkCard
import br.com.orcinus.orca.composite.timeline.test.refresh.assertIsNotInProgress
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.feature.composer.ComposerActivity
import br.com.orcinus.orca.feature.feed.FedFloatingActionButtonTag
import br.com.orcinus.orca.feature.feed.test.onSearchAction
import br.com.orcinus.orca.feature.gallery.GalleryActivity
import br.com.orcinus.orca.feature.search.SearchActivity
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.intents.test.intendBrowsingTo
import br.com.orcinus.orca.platform.intents.test.intendStartingOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class FeedTests {
  @get:Rule val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @Test
  fun navigatesToSearch() {
    intendStartingOf<SearchActivity> { composeRule.onSearchAction().performClick() }
  }

  @Test
  fun refreshes() {
    composeRule.onTimeline().performTouchInput(TouchInjectionScope::swipeDown)
    composeRule.onRefreshIndicator().assertIsNotInProgress()
  }

  @Test
  fun navigatesToPostLink() {
    runTest {
      intendBrowsingTo(
        Instance.sample.postProvider
          .provideAll()
          .first()
          .firstNotNullOf { it.content.highlight }
          .uri
      ) {
        composeRule.onTimeline().performScrollToPostPreviewWithLinkCard()
        composeRule.onLinkCards().onFirst().performClick()
      }
    }
  }

  @Test
  fun navigatesToGalleryOnThumbnailClick() {
    intendStartingOf<GalleryActivity> {
      with(composeRule) {
        onTimeline().performScrollToPostPreviewWithGalleryPreview {
          onThumbnails().onFirst().performClick()
        }
      }
    }
  }

  @Test
  fun navigatesToComposerOnFabClick() {
    intendStartingOf<ComposerActivity> {
      composeRule.onNodeWithTag(FedFloatingActionButtonTag).performClick()
    }
  }
}
