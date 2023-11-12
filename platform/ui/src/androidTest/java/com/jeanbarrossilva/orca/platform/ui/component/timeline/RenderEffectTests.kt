package com.jeanbarrossilva.orca.platform.ui.component.timeline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import org.junit.Rule
import org.junit.Test

internal class RenderEffectTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun triggers() {
    var hasBeenTriggered = false
    composeRule.setContent { RenderEffect { hasBeenTriggered = true } }
    assertThat(hasBeenTriggered).isTrue()
  }

  @Test
  fun triggersOnce() {
    var triggerCount = 0
    var isVisible by mutableStateOf(true)
    composeRule.setContent {
      if (isVisible) {
        RenderEffect { triggerCount++ }
      }
    }
    repeat(2) { isVisible = !isVisible }
    assertThat(triggerCount).isEqualTo(1)
  }
}
