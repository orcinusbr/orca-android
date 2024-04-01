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

package com.jeanbarrossilva.orca.feature.gallery.ui

import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import assertk.assertThat
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.composite.timeline.test.stat.activateable.favorite.onFavoriteStat
import com.jeanbarrossilva.orca.composite.timeline.test.stat.activateable.repost.onRepostStat
import com.jeanbarrossilva.orca.composite.timeline.test.stat.onCommentStat
import com.jeanbarrossilva.orca.feature.gallery.test.ui.onCloseActionButton
import com.jeanbarrossilva.orca.feature.gallery.test.ui.onPager
import com.jeanbarrossilva.orca.feature.gallery.test.ui.page.onPage
import com.jeanbarrossilva.orca.feature.gallery.test.ui.performScrollToEachPage
import com.jeanbarrossilva.orca.feature.gallery.ui.test.onActions
import com.jeanbarrossilva.orca.feature.gallery.ui.test.onDownloadItem
import com.jeanbarrossilva.orca.feature.gallery.ui.test.onOptionsButton
import com.jeanbarrossilva.orca.feature.gallery.ui.test.onOptionsMenu
import com.jeanbarrossilva.orca.feature.gallery.ui.test.waitForDoubleTapTimeout
import com.jeanbarrossilva.orca.feature.gallery.ui.test.zoom.assertIsZoomedIn
import com.jeanbarrossilva.orca.feature.gallery.ui.test.zoom.assertIsZoomedOut
import com.jeanbarrossilva.orca.feature.gallery.ui.test.zoom.performZoomIn
import com.jeanbarrossilva.orca.feature.gallery.ui.test.zoom.performZoomOut
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import org.junit.Rule
import org.junit.Test

internal class GalleryTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun closes() {
    var isClosed = false
    composeRule
      .apply { setContent { AutosTheme { SampleGallery(onClose = { isClosed = true }) } } }
      .onCloseActionButton()
      .performClick()
    assertThat(isClosed).isTrue()
  }

  @Test
  fun downloads() {
    var isDownloaded = false
    composeRule
      .apply { setContent { AutosTheme { SampleGallery(onDownload = { isDownloaded = true }) } } }
      .run {
        onOptionsButton().performClick()
        onDownloadItem().performClick()
      }
    assertThat(isDownloaded).isTrue()
  }

  @Test
  fun hidesOptionsMenuOnDownload() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .apply {
        onOptionsButton().performClick()
        onDownloadItem().performClick()
      }
      .onOptionsMenu()
      .assertIsNotDisplayed()
  }

  @Test
  fun comments() {
    var hasCommented = false
    composeRule
      .apply { setContent { AutosTheme { SampleGallery(onComment = { hasCommented = true }) } } }
      .onCommentStat()
      .performClick()
    assertThat(hasCommented).isTrue()
  }

  @Test
  fun favorites() {
    var isFavorited = false
    composeRule
      .apply { setContent { AutosTheme { SampleGallery(onFavorite = { isFavorited = true }) } } }
      .onFavoriteStat()
      .performClick()
    assertThat(isFavorited).isTrue()
  }

  @Test
  fun reposts() {
    var isReposted = false
    composeRule
      .apply { setContent { AutosTheme { SampleGallery(onRepost = { isReposted = true }) } } }
      .onRepostStat()
      .performClick()
    assertThat(isReposted).isTrue()
  }

  @Test
  fun hidesActionsOnClick() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .run {
        onPage().performClick()
        waitForDoubleTapTimeout()
        onActions().assertIsNotDisplayed()
      }
  }

  @Test
  fun swipesThroughEachPage() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .run { onPager().performScrollToEachPage { assertIsDisplayed() } }
  }

  @Test
  fun doubleClickZoomsIntoAndOutOfEachPage() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .run {
        onPager().performScrollToEachPage {
          performTouchInput(TouchInjectionScope::doubleClick)
            .assertIsZoomedIn()
            .performTouchInput(TouchInjectionScope::doubleClick)
            .assertIsZoomedOut()
        }
      }
  }

  @Test
  fun pinchZoomsIntoAndOutOfEachPage() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .run {
        onPager().performScrollToEachPage {
          performZoomIn().assertIsZoomedIn().performZoomOut().assertIsZoomedOut()
        }
      }
  }
}
