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

package br.com.orcinus.orca.composite.timeline

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.refresh.Refresh
import br.com.orcinus.orca.composite.timeline.test.onRefreshIndicator
import br.com.orcinus.orca.composite.timeline.test.onTimeline
import br.com.orcinus.orca.composite.timeline.test.performScrollToBottom
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.test.instance.SampleInstanceTestRule
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.core.sample
import com.jeanbarrossilva.loadable.list.ListLoadable
import kotlin.time.Duration.Companion.days
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class TimelineTests {
  @get:Rule val sampleInstanceRule = SampleInstanceTestRule(Instance.sample)
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsEmptyMessageWhenListLoadableIsEmpty() {
    composeRule.setContent { AutosTheme { Timeline(ListLoadable.Empty()) } }
    composeRule.onNodeWithTag(EmptyTimelineMessageTag).assertIsDisplayed()
  }

  @Test
  fun showsRefreshIndicatorForeverWhenRefreshIsIndefinite() {
    composeRule.setContent { Timeline(onNext = {}, refresh = Refresh.Indefinite) {} }
    composeRule.mainClock.advanceTimeBy(256.days.inWholeMilliseconds)
    composeRule.onRefreshIndicator().assertIsDisplayed()
  }

  @Test
  fun showsDividerWhenHeaderIsNotAddedOnPostPreviewBeforeLastOne() {
    composeRule.setContent { AutosTheme { Timeline(PostPreview.samples.take(2)) } }
    composeRule
      .onTimeline()
      .onChildren()
      .onFirst()
      .onSiblings()
      .filter(hasTestTag(TimelineDividerTag))
      .assertCountEquals(0)
  }

  @Test
  fun showsDividersWhenHeaderIsAddedOnPostPreviewBeforeLastOne() {
    composeRule.setContent { AutosTheme { Timeline(PostPreview.samples.take(2)) {} } }
    composeRule
      .onTimeline()
      .onChildren()[1]
      .onSiblings()
      .filter(hasTestTag(TimelineDividerTag))
      .assertCountEquals(1)
  }

  @Test
  fun doesNotShowDividersOnLastPostPreview() {
    composeRule.setContent { AutosTheme { Timeline(PostPreview.samples.take(1)) } }
    composeRule
      .onTimeline()
      .onChildren()
      .filter(hasTestTag(TimelineDividerTag))
      .assertCountEquals(0)
  }

  @Test
  fun providesNextIndexWhenReachingBottom() {
    val indices = mutableStateListOf<Int>()
    composeRule.setContent {
      Timeline(
        onNext = {
          if (indices.size < 2) {
            indices += it
          }
        }
      ) {
        items(indices) { Spacer(Modifier.fillParentMaxSize()) }
      }
    }
    composeRule.onTimeline().performScrollToBottom().performScrollToBottom()
    assertThat(indices).containsExactly(1, 2)
  }

  @Test
  fun doesNotProvideNextIndexWhenReachingBottomMultipleTimesAndNoItemsHaveBeenAdded() {
    val indices = hashSetOf<Int>()
    composeRule
      .apply {
        setContent {
          Timeline(onNext = { indices += it }) { item { Spacer(Modifier.fillParentMaxSize()) } }
        }
      }
      .onTimeline()
      .performScrollToBottom()
      .performTouchInput(TouchInjectionScope::swipeDown)
      .performScrollToBottom()
    assertThat(indices).isEqualTo(hashSetOf(1))
  }
}
