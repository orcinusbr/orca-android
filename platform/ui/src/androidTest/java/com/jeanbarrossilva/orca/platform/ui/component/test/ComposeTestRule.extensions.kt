package com.jeanbarrossilva.orca.platform.ui.component.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.ui.component.stat.ACTIVATEABLE_STAT_ICON_TAG
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconDefaults

/** [SemanticsNodeInteraction] of an [ActivateableStatIconDefaults]. **/
internal fun ComposeTestRule.onActivateableStatIcon(): SemanticsNodeInteraction {
    return onNodeWithTag(ACTIVATEABLE_STAT_ICON_TAG)
}
