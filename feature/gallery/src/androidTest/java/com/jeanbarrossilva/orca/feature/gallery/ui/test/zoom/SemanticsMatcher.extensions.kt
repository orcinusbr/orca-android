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

package com.jeanbarrossilva.orca.feature.gallery.ui.test.zoom

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.onParent
import androidx.compose.ui.unit.Density
import androidx.test.platform.app.InstrumentationRegistry

/**
 * [SemanticsMatcher] that matches a [SemanticsNode] whose [predicate] is `true`.
 *
 * @param description Describes what is being matched.
 * @param predicate Determines whether a [SemanticsNode] is matched by the returned
 *   [SemanticsMatcher] given its parent's and its own unclipped in-root bounds.
 */
context(SemanticsNodeInteraction)

internal fun unclippedBoundsMatcher(
  description: String,
  predicate: (parentBounds: Rect, bounds: Rect) -> Boolean
): SemanticsMatcher {
  return SemanticsMatcher(description) {
    val context = InstrumentationRegistry.getInstrumentation().context
    val density = Density(context)
    val bounds = with(density) { getUnclippedBoundsInRoot().toRect() }
    val parentBounds = with(density) { onParent().getUnclippedBoundsInRoot().toRect() }
    predicate(parentBounds, bounds)
  }
}
