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
