package com.jeanbarrossilva.orca.platform.ui.component.timeline

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.test.junit4.createComposeRule
import assertk.assertThat
import assertk.assertions.isFalse
import com.jeanbarrossilva.orca.platform.ui.component.timeline.bottom.renderEffect
import org.junit.Rule
import org.junit.Test

internal class RenderEffectTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun doesNotRunCallbackWhenRenderingTwice() {
    var hasBeenTriggered = false
    composeRule.setContent { LazyColumn { renderEffect { hasBeenTriggered = true } } }
    assertThat(hasBeenTriggered).isFalse()
  }
}
