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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.placement

import androidx.compose.ui.layout.Placeable

/**
 * Places the given [placements].
 *
 * @param placements [Placement]s to be placed.
 * @param orientation [Orientation] in which the [placements] will be placed.
 */
internal fun Placeable.PlacementScope.place(placements: List<Placement>, orientation: Orientation) {
  placements.forEach { place(it, orientation) }
}

/**
 * Places the given [placement].
 *
 * @param placement [Placement] to be placed.
 * @param orientation [Orientation] in which the [placement] will be placed.
 */
internal fun Placeable.PlacementScope.place(placement: Placement, orientation: Orientation) {
  val offset = orientation.getOffset(placement)
  placement.placeable.place(offset)
}
