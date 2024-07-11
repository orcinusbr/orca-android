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

package br.com.orcinus.orca.composite.timeline.test

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import br.com.orcinus.orca.composite.timeline.RenderEffectTag
import br.com.orcinus.orca.composite.timeline.Timeline
import br.com.orcinus.orca.composite.timeline.TimelineTag

/** [SemanticsMatcher] that matches a [Timeline]. */
fun isTimeline(): SemanticsMatcher {
  return SemanticsMatcher("is Timeline") {
    it.config.getOrNull(SemanticsProperties.TestTag) == TimelineTag
  }
}

/** [SemanticsMatcher] that matches a [br.com.orcinus.orca.composite.timeline.RenderEffect]. */
internal fun isRenderEffect(): SemanticsMatcher {
  return SemanticsMatcher("is RenderEffect") {
    it.config.getOrNull(SemanticsProperties.TestTag) == RenderEffectTag
  }
}
