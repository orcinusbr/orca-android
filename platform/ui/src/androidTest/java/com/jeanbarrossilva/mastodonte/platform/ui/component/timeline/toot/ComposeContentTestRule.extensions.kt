package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag

/** [SemanticsNodeInteraction] of [TootPreview]'s body node. **/
internal fun ComposeContentTestRule.onTootPreviewBody(): SemanticsNodeInteraction {
    return onNodeWithTag(TOOT_PREVIEW_BODY_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s comment count stat node. **/
internal fun ComposeContentTestRule.onTootPreviewCommentCountStat(): SemanticsNodeInteraction {
    return onNodeWithTag(TOOT_PREVIEW_COMMENT_COUNT_STAT_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s favorite count stat node. **/
internal fun ComposeContentTestRule.onTootPreviewFavoriteCountStat(): SemanticsNodeInteraction {
    return onNodeWithTag(TOOT_PREVIEW_FAVORITE_COUNT_STAT_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s metadata node. **/
internal fun ComposeContentTestRule.onTootPreviewMetadata(): SemanticsNodeInteraction {
    return onNodeWithTag(TOOT_PREVIEW_METADATA_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s name node. **/
internal fun ComposeContentTestRule.onTootPreviewName(): SemanticsNodeInteraction {
    return onNodeWithTag(TOOT_PREVIEW_NAME_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s reblog count stat node. **/
internal fun ComposeContentTestRule.onTootPreviewReblogCountStat(): SemanticsNodeInteraction {
    return onNodeWithTag(TOOT_PREVIEW_REBLOG_COUNT_STAT_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [TootPreview]'s share action node. **/
internal fun ComposeContentTestRule.onTootPreviewShareAction(): SemanticsNodeInteraction {
    return onNodeWithTag(TOOT_PREVIEW_SHARE_ACTION_TAG, useUnmergedTree = true)
}
