package com.jeanbarrossilva.orca.platform.ui.test.component.timeline

import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
import org.junit.Rule
import org.junit.Test

internal class SemanticsMatcherExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun findsRenderEffectNode() {
    composeRule.setContent { Timeline(onNext = {}) { item { Spacer(Modifier) } } }
    composeRule.onNode(isRenderEffect()).assertExists()
  }
}
