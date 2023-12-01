/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_BODY_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_COMMENT_COUNT_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_METADATA_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_NAME_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_REBLOG_COUNT_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_REBLOG_METADATA_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_SHARE_ACTION_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.stat.POST_PREVIEW_FAVORITE_STAT_TAG

/** [SemanticsNodeInteraction] of [PostPreview]'s body node. */
internal fun ComposeTestRule.onPostPreviewBody(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_BODY_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [PostPreview]'s comment count stat node. */
internal fun ComposeTestRule.onPostPreviewCommentCountStat(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_COMMENT_COUNT_STAT_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [PostPreview]'s favorite count stat node. */
internal fun ComposeTestRule.onPostPreviewFavoriteCountStat(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_FAVORITE_STAT_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [PostPreview]'s metadata node. */
internal fun ComposeTestRule.onPostPreviewMetadata(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_METADATA_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [PostPreview]'s name node. */
internal fun ComposeTestRule.onPostPreviewName(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_NAME_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [PostPreview]'s reblog count stat node. */
internal fun ComposeTestRule.onPostPreviewReblogCountStat(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_REBLOG_COUNT_STAT_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [PostPreview]'s reblog metadata node. */
internal fun ComposeTestRule.onPostPreviewReblogMetadata(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_REBLOG_METADATA_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [PostPreview]'s share action node. */
internal fun ComposeTestRule.onPostPreviewShareAction(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_SHARE_ACTION_TAG, useUnmergedTree = true)
}
