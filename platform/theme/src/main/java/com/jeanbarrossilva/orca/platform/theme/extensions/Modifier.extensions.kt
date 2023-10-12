package com.jeanbarrossilva.orca.platform.theme.extensions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.Borders

/**
 * Applies a [BorderStroke], shaped by the given [shape], when they are applicable.
 *
 * @param shape [Shape] of the [BorderStroke] to be applied.
 * @see Borders.areApplicable
 */
fun Modifier.border(shape: Shape): Modifier {
  return composed { if (Borders.areApplicable) border(OrcaTheme.borders.default, shape) else this }
}
