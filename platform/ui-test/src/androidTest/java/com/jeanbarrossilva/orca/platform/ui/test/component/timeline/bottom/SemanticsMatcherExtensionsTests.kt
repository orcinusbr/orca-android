package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.bottom

import androidx.compose.ui.test.junit4.createComposeRule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
import org.junit.Rule
import org.junit.Test

internal class SemanticsMatcherExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun findsRenderEffect() {
    composeRule.setContent { Timeline(onNext = {}) {} }
    composeRule.onNode(isRenderEffect()).assertExists()
  }
}
