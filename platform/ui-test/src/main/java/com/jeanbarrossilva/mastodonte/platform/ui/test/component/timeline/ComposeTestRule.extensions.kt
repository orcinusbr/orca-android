package com.jeanbarrossilva.mastodonte.platform.ui.test.component.timeline

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.TIMELINE_TAG

/** [SemanticsNodeInteraction] of a [Timeline] node. **/
fun ComposeTestRule.onTimeline(): SemanticsNodeInteraction {
    return onNodeWithTag(TIMELINE_TAG)
}
