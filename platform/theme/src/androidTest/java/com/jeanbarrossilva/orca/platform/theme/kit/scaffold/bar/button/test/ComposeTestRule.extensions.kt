package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.BUTTON_BAR_TAG
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.ButtonBar

/** [SemanticsNodeInteraction] of a [ButtonBar]. **/
internal fun ComposeTestRule.onButtonBar(): SemanticsNodeInteraction {
    return onNodeWithTag(BUTTON_BAR_TAG)
}
