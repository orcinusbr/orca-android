package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.bottom

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import com.jeanbarrossilva.orca.platform.ui.component.timeline.bottom.RENDER_EFFECT_TAG

/**
 * [SemanticsMatcher] that matches a
 * [com.jeanbarrossilva.orca.platform.ui.component.timeline.bottom.RenderEffect].
 */
internal fun isRenderEffect(): SemanticsMatcher {
  return SemanticsMatcher("is RenderEffect") {
    it.config.getOrNull(SemanticsProperties.TestTag) == RENDER_EFFECT_TAG
  }
}
