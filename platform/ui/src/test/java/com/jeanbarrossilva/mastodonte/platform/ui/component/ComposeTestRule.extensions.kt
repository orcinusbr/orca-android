package com.jeanbarrossilva.mastodonte.platform.ui.component

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.mastodonte.platform.ui.component.stat.ACTIVATEABLE_STAT_ICON_TAG

/** [SemanticsNodeInteraction] of an [ActivateableStatIcon]. **/
internal fun ComposeTestRule.onActivateableStatIcon(): SemanticsNodeInteraction {
    return onNodeWithTag(ACTIVATEABLE_STAT_ICON_TAG)
}
