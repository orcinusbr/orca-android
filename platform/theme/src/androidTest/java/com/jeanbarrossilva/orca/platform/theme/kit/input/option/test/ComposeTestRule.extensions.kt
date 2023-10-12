package com.jeanbarrossilva.orca.platform.theme.kit.input.option.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.OPTION_TAG
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.Option

/** [SemanticsNodeInteraction] of an [Option]. */
internal fun ComposeTestRule.onOption(): SemanticsNodeInteraction {
  return onNodeWithTag(OPTION_TAG)
}

/** [SemanticsNodeInteractionCollection] of [Option]s. */
internal fun ComposeTestRule.onOptions(): SemanticsNodeInteractionCollection {
  return onAllNodesWithTag(OPTION_TAG)
}
