package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.placement

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints

/**
 * Converts this [Measurable] into a [Placement].
 *
 * @param constraints Measuring that will limit this [Measurable]'s measured size.
 * @param offset Amount in pixels by which the [Placement] will be offset.
 */
internal fun Measurable.toPlacement(constraints: Constraints, offset: Int): Placement {
  val placeable = measure(constraints)
  return Placement(placeable, offset)
}
