package com.jeanbarrossilva.orca.platform.ui.test.component.timeline

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.ui.component.timeline.TIMELINE_REFRESH_INDICATOR
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

/** [SemanticsNodeInteraction] of a [Timeline]'s refresh indicator. */
fun ComposeTestRule.onRefreshIndicator(): SemanticsNodeInteraction {
  return onNodeWithTag(TIMELINE_REFRESH_INDICATOR)
}

/** [SemanticsNodeInteraction] of a [Timeline] node. */
fun ComposeTestRule.onTimeline(): SemanticsNodeInteraction {
  return onNode(isTimeline())
}
