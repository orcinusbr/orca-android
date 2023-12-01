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

package com.jeanbarrossilva.orca.platform.ui.test.component.timeline

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import com.jeanbarrossilva.orca.platform.ui.component.timeline.RENDER_EFFECT_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.TIMELINE_TAG
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

/**
 * [SemanticsMatcher] that matches a
 * [com.jeanbarrossilva.orca.platform.ui.component.timeline.RenderEffect].
 */
internal fun isRenderEffect(): SemanticsMatcher {
  return SemanticsMatcher("is RenderEffect") {
    it.config.getOrNull(SemanticsProperties.TestTag) == RENDER_EFFECT_TAG
  }
}

/** [SemanticsMatcher] that matches a [Timeline]. */
internal fun isTimeline(): SemanticsMatcher {
  return SemanticsMatcher("is Timeline") {
    it.config.getOrNull(SemanticsProperties.TestTag) == TIMELINE_TAG
  }
}
