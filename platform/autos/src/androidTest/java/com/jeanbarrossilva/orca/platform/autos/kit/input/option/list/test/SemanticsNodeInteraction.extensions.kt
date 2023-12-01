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

package com.jeanbarrossilva.orca.platform.autos.kit.input.option.list.test

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

/**
 * Asserts that the [SemanticsNode] has been clipped by the given [shape].
 *
 * @param shape [Shape] to assert that it's the one by which the [SemanticsNode] is shaped.
 */
internal fun SemanticsNodeInteraction.assertIsShapedBy(shape: Shape): SemanticsNodeInteraction {
  return assert(isShapedBy(shape))
}
