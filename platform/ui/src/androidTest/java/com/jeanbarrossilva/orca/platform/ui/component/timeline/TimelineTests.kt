package com.jeanbarrossilva.orca.platform.ui.component.timeline

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onSiblings
import assertk.assertThat
import assertk.assertions.containsExactly
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.performScrollToBottom
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time.Time4JTestRule
import org.junit.Rule
import org.junit.Test

internal class TimelineTests {
  @get:Rule val sampleInstanceRule = SampleInstanceTestRule()

  @get:Rule val time4JRule = Time4JTestRule()

  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsEmptyMessageWhenListLoadableIsEmpty() {
    composeRule.setContent { AutosTheme { Timeline(ListLoadable.Empty()) } }
    composeRule.onNodeWithTag(EMPTY_TIMELINE_MESSAGE_TAG).assertIsDisplayed()
  }

  @Test
  fun showsDividerWhenHeaderIsNotAddedOnPostPreviewBeforeLastOne() {
    composeRule.setContent { AutosTheme { Timeline(PostPreview.samples.take(2)) } }
    composeRule
      .onNodeWithTag(TIMELINE_TAG)
      .onChildren()
      .onFirst()
      .onSiblings()
      .filter(hasTestTag(TIMELINE_DIVIDER_TAG))
      .assertCountEquals(0)
  }

  @Test
  fun showsDividersWhenHeaderIsAddedOnPostPreviewBeforeLastOne() {
    composeRule.setContent { AutosTheme { Timeline(PostPreview.samples.take(2)) {} } }
    composeRule
      .onNodeWithTag(TIMELINE_TAG)
      .onChildren()[1]
      .onSiblings()
      .filter(hasTestTag(TIMELINE_DIVIDER_TAG))
      .assertCountEquals(1)
  }

  @Test
  fun doesNotShowDividersOnLastPostPreview() {
    composeRule.setContent { AutosTheme { Timeline(PostPreview.samples.take(1)) } }
    composeRule
      .onTimeline()
      .onChildren()
      .filter(hasTestTag(TIMELINE_DIVIDER_TAG))
      .assertCountEquals(0)
  }

  @Test
  fun providesNextIndexWhenReachingBottom() {
    val indices = mutableStateListOf<Int>()
    composeRule.setContent {
      Timeline(onNext = { indices += it }) {
        items(indices) { Spacer(Modifier.fillParentMaxSize()) }
      }
    }
    composeRule.onTimeline().performScrollToBottom().performScrollToBottom()
    assertThat(indices).containsExactly(0, 1, 2)
  }
}
