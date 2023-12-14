/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_BODY_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_METADATA_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_NAME_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_REBLOG_METADATA_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview

/** [SemanticsNodeInteraction] of [PostPreview]'s body node. */
internal fun ComposeTestRule.onPostPreviewBody(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_BODY_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [PostPreview]'s metadata node. */
internal fun ComposeTestRule.onPostPreviewMetadata(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_METADATA_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [PostPreview]'s name node. */
internal fun ComposeTestRule.onPostPreviewName(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_NAME_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of [PostPreview]'s reblog metadata node. */
internal fun ComposeTestRule.onPostPreviewReblogMetadata(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_REBLOG_METADATA_TAG, useUnmergedTree = true)
}
