package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.refresh

import androidx.compose.ui.test.junit4.createComposeRule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.orca.platform.ui.component.timeline.refresh.Refresh
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onRefreshIndicator
import org.junit.Rule
import org.junit.Test

internal class SemanticsNodeInteractionExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test(expected = AssertionError::class)
  fun throwsWhenAssertingThatNodeIsNotInProgressWhenItIs() {
    composeRule.setContent { Timeline(onNext = {}, refresh = Refresh.Indefinite) {} }
    composeRule.onRefreshIndicator().assertIsNotInProgress()
  }

  @Test
  fun doesNotThrowWhenAssertingThatNodeIsNotInProgressWhenItIsNot() {
    composeRule.setContent { Timeline(onNext = {}) {} }
    composeRule.onRefreshIndicator().assertIsNotInProgress()
  }
}
