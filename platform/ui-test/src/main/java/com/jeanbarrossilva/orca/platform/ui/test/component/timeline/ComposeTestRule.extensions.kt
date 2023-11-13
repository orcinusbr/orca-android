package com.jeanbarrossilva.orca.platform.ui.test.component.timeline

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

/** [SemanticsNodeInteraction] of a [Timeline] node. */
fun ComposeTestRule.onTimeline(): SemanticsNodeInteraction {
  return onNode(isTimeline())
}
