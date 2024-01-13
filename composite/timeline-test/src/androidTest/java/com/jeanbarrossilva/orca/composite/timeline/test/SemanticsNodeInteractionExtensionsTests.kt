/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.composite.timeline.test

import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onRoot
import com.jeanbarrossilva.orca.composite.timeline.Timeline
import org.junit.Rule
import org.junit.Test

internal class SemanticsNodeInteractionExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test(expected = AssertionError::class)
  fun throwsWhenScrollingToBottomOfNonTimelineNode() {
    composeRule.setContent {}
    composeRule.onRoot().performScrollToBottom().assertIsDisplayed()
  }

  @Test
  fun scrollsToTimelineBottom() {
    composeRule.setContent {
      Timeline(onNext = {}) { items(2) { Spacer(Modifier.fillParentMaxSize()) } }
    }
    composeRule
      .onTimeline()
      .performScrollToBottom()
      .onChildren()
      .assertCountEquals(1)
      .filterToOne(isRenderEffect())
      .assertExists()
  }
}
