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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.feature.gallery.test.assertIsZoomedIn
import com.jeanbarrossilva.orca.feature.gallery.test.assertIsZoomedOut
import com.jeanbarrossilva.orca.feature.gallery.test.onPage
import com.jeanbarrossilva.orca.feature.gallery.test.onPager
import com.jeanbarrossilva.orca.feature.gallery.test.performScrollToPageAt
import com.jeanbarrossilva.orca.feature.gallery.test.performZoomIn
import com.jeanbarrossilva.orca.feature.gallery.test.performZoomOut
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.formatted
import org.junit.Rule
import org.junit.Test

internal class SemanticsNodeInteractionExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun performsZoomIn() {
    composeRule.apply { setContent { Gallery() } }.onPage().performZoomIn().assertIsZoomedIn()
  }

  @Test(expected = AssertionError::class)
  fun throwsWhenAssertingThatIsZoomedInWhenItIsNot() {
    composeRule.apply { setContent { Gallery() } }.onPage().performZoomOut().assertIsZoomedIn()
  }

  @Test
  fun assertsIsZoomedIn() {
    composeRule.apply { setContent { Gallery() } }.onPage().performZoomIn().assertIsZoomedIn()
  }

  @Test
  fun performsZoomOut() {
    composeRule
      .apply { setContent { Gallery() } }
      .onPage()
      .performZoomIn()
      .performZoomOut()
      .assertIsZoomedOut()
  }

  @Test(expected = AssertionError::class)
  fun throwsWhenAssertingThatIsZoomedOutWhenItIsNot() {
    composeRule.apply { setContent { Gallery() } }.onPage().performZoomIn().assertIsZoomedOut()
  }

  @Test
  fun assertsIsZoomedOut() {
    composeRule
      .apply { setContent { Gallery() } }
      .onPage()
      .performZoomIn()
      .performZoomOut()
      .assertIsZoomedOut()
  }

  @Test(expected = AssertionError::class)
  fun throwsWhenScrollingToPageOfNonGalleryPagerNode() {
    composeRule
      .apply {
        setContent {
          @OptIn(ExperimentalFoundationApi::class)
          HorizontalPager(rememberPagerState { 2 }) { Text("$it") }
        }
      }
      .run { onRoot().performScrollToPageAt(0) }
  }

  @Test
  fun scrollsToPageOfGalleryPager() {
    val context = InstrumentationRegistry.getInstrumentation().context
    composeRule
      .apply { setContent { Gallery() } }
      .run {
        onPager().performScrollToPageAt(0)
        onPage()
      }
      .assertContentDescriptionEquals(
        context.getString(R.string.feature_gallery_attachment, 2.formatted)
      )
  }
}
