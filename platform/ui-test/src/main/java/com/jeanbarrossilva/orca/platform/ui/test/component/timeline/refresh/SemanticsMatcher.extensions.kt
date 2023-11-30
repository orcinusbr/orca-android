package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.refresh

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import com.jeanbarrossilva.orca.platform.ui.component.timeline.refresh.InProgress

/** [SemanticsMatcher] that matches a [SemanticsNode] that's in a temporarily active state. */
internal fun isInProgress(): SemanticsMatcher {
  return SemanticsMatcher("is in progress") {
    it.config.getOrElse(SemanticsProperties.InProgress) { false }
  }
}
