package com.jeanbarrossilva.orca.platform.ui.component.timeline

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.time.Time4JTestRule
import org.junit.Rule
import org.junit.Test

internal class TimelineTests {
  @get:Rule val sampleInstanceRule = SampleInstanceTestRule()

  @get:Rule val time4JRule = Time4JTestRule()

  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsEmptyMessageWhenListLoadableIsEmpty() {
    composeRule.setContent { OrcaTheme { Timeline(ListLoadable.Empty()) } }
    composeRule.onNodeWithTag(EMPTY_TIMELINE_MESSAGE_TAG).assertIsDisplayed()
  }

  @Test
  fun showsDividerWhenHeaderIsNotAddedOnTootPreviewBeforeLastOne() {
    composeRule.setContent {
      OrcaTheme { PopulatedTimeline(tootPreviews = TootPreview.samples.take(2)) }
    }
    composeRule
      .onNodeWithTag(TIMELINE_TAG)
      .onChildren()
      .onFirst()
      .onSiblings()
      .filter(hasTestTag(TIMELINE_DIVIDER_TAG))
      .assertCountEquals(0)
  }

  @Test
  fun showsDividersWhenHeaderIsAddedOnTootPreviewBeforeLastOne() {
    composeRule.setContent {
      OrcaTheme { PopulatedTimeline(tootPreviews = TootPreview.samples.take(2)) {} }
    }
    composeRule
      .onNodeWithTag(TIMELINE_TAG)
      .onChildren()[1]
      .onSiblings()
      .filter(hasTestTag(TIMELINE_DIVIDER_TAG))
      .assertCountEquals(1)
  }

  @Test
  fun doesNotShowDividersOnLastTootPreview() {
    composeRule.setContent {
      OrcaTheme { PopulatedTimeline(tootPreviews = TootPreview.samples.take(1)) }
    }
    composeRule
      .onNodeWithTag(TIMELINE_TAG)
      .onChild()
      .onSiblings()
      .filter(hasTestTag(TIMELINE_DIVIDER_TAG))
      .assertCountEquals(0)
  }

  @Test
  fun loadsNextTootsWhenReachingBottom() {
    val resources = InstrumentationRegistry.getInstrumentation().context?.resources
    val configuration = resources?.configuration
    val screenWidth = configuration?.screenWidthDp?.dp ?: Dp.Unspecified
    val screenHeight = configuration?.screenHeightDp?.dp ?: Dp.Unspecified
    val screenDpSize = DpSize(screenWidth, screenHeight)
    val itemModifier = Modifier.size(screenDpSize)
    val items = mutableStateListOf<@Composable () -> Unit>({ Spacer(itemModifier) })
    composeRule.setContent {
      OrcaTheme {
        Timeline(
          onNext = {
            if (it > 0) {
              items.add { Spacer(itemModifier) }
            }
          }
        ) {
          items(items) { it() }
        }
      }
    }
    composeRule.onTimeline().performTouchInput(TouchInjectionScope::swipeUp)
    assertThat(items.size).isEqualTo(2)
  }
}
