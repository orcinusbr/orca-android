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
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.feature.gallery.test.get
import com.jeanbarrossilva.orca.feature.gallery.test.onPage
import com.jeanbarrossilva.orca.feature.gallery.test.onPager
import com.jeanbarrossilva.orca.feature.gallery.test.performScrollToPageAt
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.formatted
import org.junit.Rule
import org.junit.Test

internal class SemanticsNodeInteractionExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun getsWhetherMatcherMatchesNodeOfInteractionWhenItDoes() {
    assertThat(composeRule.apply { setContent {} }.onRoot()[isRoot()]).isTrue()
  }

  @Test
  fun getsWhetherMatcherMatchesNodeOfInteractionWhenItDoesNot() {
    assertThat(
        composeRule.apply { setContent { Text("🐋") } }.onNodeWithText("🐋")[hasTextExactly("🦈")]
      )
      .isFalse()
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
      .apply { setContent { AutosTheme { Gallery() } } }
      .run {
        onPager().performScrollToPageAt(0)
        onPage()
      }
      .assertContentDescriptionEquals(
        context.getString(R.string.feature_gallery_attachment, 2.formatted)
      )
  }
}
