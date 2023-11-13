package com.jeanbarrossilva.orca.platform.ui.test.component.timeline

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import com.jeanbarrossilva.orca.platform.ui.component.timeline.TIMELINE_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

/** [SemanticsMatcher] that matches a [Timeline]. */
internal fun isTimeline(): SemanticsMatcher {
  return SemanticsMatcher("is Timeline") {
    it.config.getOrNull(SemanticsProperties.TestTag) == TIMELINE_TAG
  }
}
