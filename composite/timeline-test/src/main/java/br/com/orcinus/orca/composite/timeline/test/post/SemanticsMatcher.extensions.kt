/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.test.post

import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.post.PostPreviewTag

/** [SemanticsMatcher] that matches a [PostPreview]. */
fun isPostPreview(): SemanticsMatcher {
  return SemanticsMatcher("is PostPreview") {
    it.config.getOrNull(SemanticsProperties.TestTag) == PostPreviewTag
  }
}

/**
 * [SemanticsMatcher] that matches a [Composable] that's linked to the given [postPreview].
 *
 * @param postPreview [PostPreview] that's expected to be linked.
 */
internal fun isPostPreview(postPreview: PostPreview): SemanticsMatcher {
  return SemanticsMatcher("PostPreview.id = ${postPreview.id}") {
    it.config[SemanticsProperties.PostPreview].id == postPreview.id
  }
}
