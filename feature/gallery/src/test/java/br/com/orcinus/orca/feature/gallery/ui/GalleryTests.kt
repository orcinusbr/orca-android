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

package br.com.orcinus.orca.feature.gallery.ui

import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import assertk.assertThat
import assertk.assertions.isTrue
import br.com.orcinus.orca.composite.timeline.test.stat.activateable.favorite.onFavoriteStat
import br.com.orcinus.orca.composite.timeline.test.stat.activateable.repost.onRepostStat
import br.com.orcinus.orca.composite.timeline.test.stat.onCommentStat
import br.com.orcinus.orca.feature.gallery.test.ui.onCloseActionButton
import br.com.orcinus.orca.feature.gallery.test.ui.onPager
import br.com.orcinus.orca.feature.gallery.test.ui.page.onPage
import br.com.orcinus.orca.feature.gallery.test.ui.performScrollToEachPage
import br.com.orcinus.orca.feature.gallery.ui.zoom.assertIsZoomedIn
import br.com.orcinus.orca.feature.gallery.ui.zoom.assertIsZoomedOut
import br.com.orcinus.orca.feature.gallery.ui.zoom.performZoomIn
import br.com.orcinus.orca.feature.gallery.ui.zoom.performZoomOut
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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
