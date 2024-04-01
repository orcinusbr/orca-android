/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.composite.timeline.post.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.composite.timeline.post.POST_PREVIEW_BODY_TAG
import com.jeanbarrossilva.orca.composite.timeline.post.POST_PREVIEW_METADATA_TAG
import com.jeanbarrossilva.orca.composite.timeline.post.POST_PREVIEW_NAME_TAG
import com.jeanbarrossilva.orca.composite.timeline.post.POST_PREVIEW_REPOST_METADATA_TAG
import com.jeanbarrossilva.orca.composite.timeline.post.PostPreview

/** [SemanticsNodeInteraction] of a [PostPreview]'s body. */
internal fun SemanticsNodeInteractionsProvider.onPostPreviewBody(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_BODY_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of a [PostPreview]'s metadata. */
internal fun SemanticsNodeInteractionsProvider.onPostPreviewMetadata(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_METADATA_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of a [PostPreview]'s name. */
internal fun SemanticsNodeInteractionsProvider.onPostPreviewName(): SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_NAME_TAG, useUnmergedTree = true)
}

/** [SemanticsNodeInteraction] of a [PostPreview]'s repost metadata. */
internal fun SemanticsNodeInteractionsProvider.onPostPreviewRepostMetadata():
  SemanticsNodeInteraction {
  return onNodeWithTag(POST_PREVIEW_REPOST_METADATA_TAG, useUnmergedTree = true)
}
