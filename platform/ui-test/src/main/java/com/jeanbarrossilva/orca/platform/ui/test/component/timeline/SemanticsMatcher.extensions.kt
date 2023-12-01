/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
