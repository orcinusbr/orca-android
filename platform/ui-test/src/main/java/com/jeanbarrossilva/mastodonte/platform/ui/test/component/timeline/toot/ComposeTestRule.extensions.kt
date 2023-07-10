package com.jeanbarrossilva.mastodonte.platform.ui.test.component.timeline.toot

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.TOOT_PREVIEW_TAG
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.TootPreview

/** [SemanticsNodeInteractionCollection] of [TootPreview] nodes. **/
fun ComposeTestRule.onTootPreviews(): SemanticsNodeInteractionCollection {
    return onAllNodesWithTag(TOOT_PREVIEW_TAG)
}

/** [SemanticsNodeInteraction] of a [TootPreview] node. **/
fun ComposeTestRule.onTootPreview(): SemanticsNodeInteraction {
    return onNodeWithTag(TOOT_PREVIEW_TAG)
}
