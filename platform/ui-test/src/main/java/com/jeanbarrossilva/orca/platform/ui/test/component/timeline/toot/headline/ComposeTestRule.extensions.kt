package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.headline

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.headline.HEADLINE_CARD_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.headline.HeadlineCard

/** [SemanticsNodeInteraction] of a [HeadlineCard]. **/
fun ComposeTestRule.onHeadlineCard(): SemanticsNodeInteraction {
    return onNodeWithTag(HEADLINE_CARD_TAG)
}

/** [SemanticsNodeInteractionCollection] of [HeadlineCard]s. **/
fun ComposeTestRule.onHeadlineCards(): SemanticsNodeInteractionCollection {
    return onAllNodesWithTag(HEADLINE_CARD_TAG)
}
