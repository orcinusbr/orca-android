package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.POST_PREVIEW_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview

/** [SemanticsMatcher] that matches a [PostPreview]. */
fun isPostPreview(): SemanticsMatcher {
  return SemanticsMatcher("is PostPreview") {
    it.config.getOrNull(SemanticsProperties.TestTag) == POST_PREVIEW_TAG
  }
}
