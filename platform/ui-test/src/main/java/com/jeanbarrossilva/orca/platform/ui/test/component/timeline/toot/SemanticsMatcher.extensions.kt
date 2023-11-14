package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TOOT_PREVIEW_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview

/** [SemanticsMatcher] that matches a [TootPreview]. */
fun isTootPreview(): SemanticsMatcher {
  return SemanticsMatcher("is TootPreview") {
    it.config.getOrNull(SemanticsProperties.TestTag) == TOOT_PREVIEW_TAG
  }
}
