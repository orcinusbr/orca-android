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

package com.jeanbarrossilva.orca.app.demo

import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.jeanbarrossilva.orca.app.demo.test.performScrollToPostPreviewWithGalleryPreview
import com.jeanbarrossilva.orca.app.demo.test.performScrollToPostPreviewWithLinkCard
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight.sample
import com.jeanbarrossilva.orca.ext.intents.intendBrowsingTo
import com.jeanbarrossilva.orca.ext.intents.intendStartingOf
import com.jeanbarrossilva.orca.feature.composer.ComposerActivity
import com.jeanbarrossilva.orca.feature.feed.FEED_FLOATING_ACTION_BUTTON_TAG
import com.jeanbarrossilva.orca.feature.gallery.GalleryActivity
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onRefreshIndicator
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.figure.gallery.thumbnail.onThumbnails
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.figure.link.onLinkCards
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.refresh.assertIsNotInProgress
import org.junit.Rule
import org.junit.Test

internal class FeedTests {
  @get:Rule val composeRule = createAndroidComposeRule<DemoOrcaActivity>()

  @Test
  fun refreshes() {
    composeRule.onTimeline().performTouchInput(TouchInjectionScope::swipeDown)
    composeRule.onRefreshIndicator().assertIsNotInProgress()
  }

  @Test
  fun navigatesToPostLink() {
    intendBrowsingTo("${Highlight.sample.url}") {
      composeRule.performScrollToPostPreviewWithLinkCard()
      composeRule.onLinkCards().onFirst().performClick()
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
      composeRule.onNodeWithTag(FEED_FLOATING_ACTION_BUTTON_TAG).performClick()
    }
  }
}
