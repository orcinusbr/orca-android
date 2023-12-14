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

import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.pinch

/**
 * Zooms from the center to both the top left and the bottom right.
 *
 * @see TouchInjectionScope.center
 * @see TouchInjectionScope.topLeft
 * @see TouchInjectionScope.bottomRight
 */
internal fun TouchInjectionScope.zoomIn() {
  pinch(start0 = center, end0 = topLeft, start1 = center, end1 = bottomRight)
}

/**
 * Zooms from both the top left and the bottom right to the center.
 *
 * @see TouchInjectionScope.topLeft
 * @see TouchInjectionScope.bottomRight
 * @see TouchInjectionScope.center
 */
internal fun TouchInjectionScope.zoomOut() {
  pinch(start0 = topLeft, end0 = center, start1 = bottomRight, end1 = center)
}
