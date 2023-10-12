package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TOOT_PREVIEW_BODY_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TOOT_PREVIEW_COMMENT_COUNT_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TOOT_PREVIEW_METADATA_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TOOT_PREVIEW_NAME_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TOOT_PREVIEW_REBLOG_COUNT_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TOOT_PREVIEW_SHARE_ACTION_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat.TOOT_PREVIEW_FAVORITE_STAT_TAG

/** [SemanticsNodeInteraction] of [TootPreview]'s body node. */
internal fun ComposeTestRule.onTootPreviewBody(): SemanticsNodeInteraction {
  return onNodeWithTag(TOOT_PREVIEW_BODY_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s comment count stat node. */
internal fun ComposeTestRule.onTootPreviewCommentCountStat(): SemanticsNodeInteraction {
  return onNodeWithTag(TOOT_PREVIEW_COMMENT_COUNT_STAT_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s favorite count stat node. */
internal fun ComposeTestRule.onTootPreviewFavoriteCountStat(): SemanticsNodeInteraction {
  return onNodeWithTag(TOOT_PREVIEW_FAVORITE_STAT_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s metadata node. */
internal fun ComposeTestRule.onTootPreviewMetadata(): SemanticsNodeInteraction {
  return onNodeWithTag(TOOT_PREVIEW_METADATA_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s name node. */
internal fun ComposeTestRule.onTootPreviewName(): SemanticsNodeInteraction {
  return onNodeWithTag(TOOT_PREVIEW_NAME_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s reblog count stat node. */
internal fun ComposeTestRule.onTootPreviewReblogCountStat(): SemanticsNodeInteraction {
  return onNodeWithTag(TOOT_PREVIEW_REBLOG_COUNT_STAT_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s share action node. */
internal fun ComposeTestRule.onTootPreviewShareAction(): SemanticsNodeInteraction {
  return onNodeWithTag(TOOT_PREVIEW_SHARE_ACTION_TAG, useUnmergedTree = true)
}
