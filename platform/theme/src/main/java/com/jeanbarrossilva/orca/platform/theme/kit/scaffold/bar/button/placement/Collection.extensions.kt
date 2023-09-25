package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.placement

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints

/** Height of all [Placement]s combined. **/
internal val Collection<Placement>.height
    get() = sumOf { placement -> placement.placeable.height }

/**
 * Maps each [Measurable] to a [Placement].
 *
 * @param constraints Measuring that will limit the [Measurable]s' measured size.
 * @param orientation [Orientation] in which the resulting [Placement]s will be placed.
 * @param spacing Size of the space between each of the [Placement]s in pixels.
 **/
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
