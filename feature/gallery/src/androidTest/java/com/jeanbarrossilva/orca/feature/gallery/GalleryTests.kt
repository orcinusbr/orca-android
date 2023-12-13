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

package com.jeanbarrossilva.orca.feature.gallery

import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import assertk.assertThat
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.feature.gallery.test.onActions
import com.jeanbarrossilva.orca.feature.gallery.test.onCloseActionButton
import com.jeanbarrossilva.orca.feature.gallery.test.onDownloadItem
import com.jeanbarrossilva.orca.feature.gallery.test.onOptionsButton
import com.jeanbarrossilva.orca.feature.gallery.test.onOptionsMenu
import com.jeanbarrossilva.orca.feature.gallery.test.onPage
import com.jeanbarrossilva.orca.feature.gallery.test.performScrollToPageAt
import com.jeanbarrossilva.orca.feature.gallery.test.waitForDoubleTapTimeout
import com.jeanbarrossilva.orca.feature.gallery.test.zoom.assertIsZoomedIn
import com.jeanbarrossilva.orca.feature.gallery.test.zoom.assertIsZoomedOut
import com.jeanbarrossilva.orca.feature.gallery.test.zoom.performZoomIn
import com.jeanbarrossilva.orca.feature.gallery.test.zoom.performZoomOut
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.`if`
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.test.component.stat.favorite.onFavoriteStat
import com.jeanbarrossilva.orca.platform.ui.test.component.stat.onCommentStat
import com.jeanbarrossilva.orca.platform.ui.test.component.stat.repost.onRepostStat
import org.junit.Rule
import org.junit.Test

internal class GalleryTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun closes() {
    var isClosed = false
    composeRule
      .apply { setContent { AutosTheme { Gallery(onClose = { isClosed = true }) } } }
      .onCloseActionButton()
      .performClick()
    assertThat(isClosed).isTrue()
  }

  @Test
  fun downloads() {
    var isDownloaded = false
    composeRule
      .apply { setContent { AutosTheme { Gallery(onDownload = { isDownloaded = true }) } } }
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
      .apply { setContent { AutosTheme { Gallery(onComment = { hasCommented = true }) } } }
      .onCommentStat()
      .performClick()
    assertThat(hasCommented).isTrue()
  }

  @Test
  fun favorites() {
    var isFavorited = false
    composeRule
      .apply { setContent { AutosTheme { Gallery(onFavorite = { isFavorited = true }) } } }
      .onFavoriteStat()
      .performClick()
    assertThat(isFavorited).isTrue()
  }

  @Test
  fun reposts() {
    var isReposted = false
    composeRule
      .apply { setContent { AutosTheme { Gallery(onRepost = { isReposted = true }) } } }
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
      .run {
        repeat(Attachment.samples.size.dec()) { index ->
          onPage().`if`(index < Attachment.samples.lastIndex) {
            onParent().performScrollToPageAt(index)
          }
        }
      }
  }

  @Test
  fun doubleClickZoomsIntoAndOutOfEachPage() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .run {
        repeat(Attachment.samples.size) { index ->
          onPage()
            .performTouchInput(TouchInjectionScope::doubleClick)
            .assertIsZoomedIn()
            .performTouchInput(TouchInjectionScope::doubleClick)
            .assertIsZoomedOut()
            .`if`(index < Attachment.samples.lastIndex) { onParent().performScrollToPageAt(index) }
        }
      }
  }

  @Test
  fun pinchZoomsIntoAndOutOfEachPage() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .run {
        repeat(Attachment.samples.size) { index ->
          onPage().performZoomIn().assertIsZoomedIn().performZoomOut().assertIsZoomedOut().`if`(
            index < Attachment.samples.lastIndex
          ) {
            onParent().performScrollToPageAt(index)
          }
        }
      }
  }
}
