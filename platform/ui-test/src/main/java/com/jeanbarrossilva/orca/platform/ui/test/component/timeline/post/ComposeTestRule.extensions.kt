package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview

/** [SemanticsNodeInteractionCollection] of [PostPreview] nodes. */
fun ComposeTestRule.onPostPreviews(): SemanticsNodeInteractionCollection {
  return onAllNodes(isPostPreview())
}

/** [SemanticsNodeInteraction] of a [PostPreview] node. */
fun ComposeTestRule.onPostPreview(): SemanticsNodeInteraction {
  return onNode(isPostPreview())
}
