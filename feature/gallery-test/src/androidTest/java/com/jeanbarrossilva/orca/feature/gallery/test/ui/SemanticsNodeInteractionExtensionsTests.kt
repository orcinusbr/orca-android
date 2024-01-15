/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.feature.gallery.test.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.composite.timeline.stat.details.formatted
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.samples
import com.jeanbarrossilva.orca.feature.gallery.R
import com.jeanbarrossilva.orca.feature.gallery.test.ui.page.onPage
import com.jeanbarrossilva.orca.feature.gallery.ui.Gallery
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.testing.asString
import org.junit.Rule
import org.junit.Test

internal class SemanticsNodeInteractionExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test(expected = AssertionError::class)
  fun throwsWhenScrollingToPageOfNonGalleryPagerNode() {
    composeRule
      .apply {
        setContent {
          @OptIn(ExperimentalFoundationApi::class)
          HorizontalPager(rememberPagerState { 2 }) { Text("$it") }
        }
      }
      .run { onRoot().performScrollToPageAfter(0) }
  }

  @Test
  fun scrollsToPageOfGalleryPager() {
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .run {
        onPager().performScrollToPageAfter(0)
        onPage()
      }
      .assertContentDescriptionEquals(R.string.feature_gallery_attachment.asString(2.formatted))
  }

  @Test
  fun scrollsToEachPageOfGalleryPager() {
    var positions = IntRange.EMPTY
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .run {
        onPager().performScrollToEachPage {
          positions = 1..it
          assertContentDescriptionEquals(R.string.feature_gallery_attachment.asString(it.formatted))
        }
      }
    assertThat(positions).isEqualTo(1..Attachment.samples.size.inc())
  }

  @Test
  fun scrollsToRemainingPagesOfGalleryPager() {
    var positions = IntRange.EMPTY
    composeRule
      .apply { setContent { AutosTheme { Gallery() } } }
      .run {
        onPager().performScrollToPageAfter(0).performScrollToEachPage {
          positions = if (it == 2) 2..2 else positions.first..it
          assertContentDescriptionEquals(R.string.feature_gallery_attachment.asString(it.formatted))
        }
      }
    assertThat(positions).isEqualTo(2..Attachment.samples.size.inc())
  }
}
