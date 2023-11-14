package com.jeanbarrossilva.orca.platform.ui.test.component.timeline

import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onRoot
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
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
