package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview

/** [SemanticsNodeInteractionCollection] of [TootPreview] nodes. */
fun ComposeTestRule.onTootPreviews(): SemanticsNodeInteractionCollection {
  return onAllNodes(isTootPreview())
}

/** [SemanticsNodeInteraction] of a [TootPreview] node. */
fun ComposeTestRule.onTootPreview(): SemanticsNodeInteraction {
  return onNode(isTootPreview())
}
