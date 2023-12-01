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
