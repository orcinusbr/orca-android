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

package com.jeanbarrossilva.orca.feature.gallery.test.zoom

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.performTouchInput

/**
 * Asserts that the node has been zoomed into.
 *
 * @see TouchInjectionScope.zoomIn
 */
internal fun SemanticsNodeInteraction.assertIsZoomedIn(): SemanticsNodeInteraction {
  return assert(
    unclippedBoundsMatcher("is zoomed in") { parentBounds, bounds ->
      bounds.top < parentBounds.top && bounds.bottom < parentBounds.bottom
    }
  )
}

/**
 * Asserts that the node has been zoomed out of.
 *
 * @see TouchInjectionScope.zoomOut
 */
internal fun SemanticsNodeInteraction.assertIsZoomedOut(): SemanticsNodeInteraction {
  return assert(
    unclippedBoundsMatcher("is zoomed out") { parentBounds, bounds ->
      bounds.top >= parentBounds.top && bounds.bottom <= parentBounds.bottom
    }
  )
}

/**
 * Zooms into the [SemanticsNode].
 *
 * @see TouchInjectionScope.zoomIn
 */
internal fun SemanticsNodeInteraction.performZoomIn(): SemanticsNodeInteraction {
  return performTouchInput(TouchInjectionScope::zoomIn)
}

/**
 * Zooms out of the [SemanticsNode].
 *
 * @see TouchInjectionScope.zoomOut
 */
internal fun SemanticsNodeInteraction.performZoomOut(): SemanticsNodeInteraction {
  return performTouchInput(TouchInjectionScope::zoomOut)
}
