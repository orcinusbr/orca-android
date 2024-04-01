/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.placement

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints

/** Height of all [Placement]s combined. */
internal val Collection<Placement>.height
  get() = sumOf { placement -> placement.placeable.height }

/**
 * Maps each [Measurable] to a [Placement].
 *
 * @param constraints Measuring that will limit the [Measurable]s' measured size.
 * @param orientation [Orientation] in which the resulting [Placement]s will be placed.
 * @param spacing Size of the space between each of the [Placement]s in pixels.
 */
internal fun Collection<Measurable>.mapToPlacement(
  constraints: Constraints,
  orientation: Orientation,
  spacing: Int
): List<Placement> {
  var offset = 0
  return map { measurable ->
    measurable.toPlacement(constraints, offset).also { placement ->
      offset += orientation.getDimension(placement.placeable) + spacing
    }
  }
}
