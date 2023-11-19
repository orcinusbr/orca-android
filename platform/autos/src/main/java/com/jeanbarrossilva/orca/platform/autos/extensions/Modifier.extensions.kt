package com.jeanbarrossilva.orca.platform.autos.extensions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import com.jeanbarrossilva.orca.autos.borders.Borders
import com.jeanbarrossilva.orca.platform.autos.autos.borders.areApplicable
import com.jeanbarrossilva.orca.platform.autos.autos.borders.asBorderStroke
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/**
 * Applies a [BorderStroke], shaped by the given [shape], when they are applicable.
 *
 * @param shape [Shape] of the [BorderStroke] to be applied.
 * @see Borders.areApplicable
 */
fun Modifier.border(shape: Shape): Modifier {
  return composed {
    if (Borders.areApplicable) border(AutosTheme.borders.default.asBorderStroke, shape) else this
  }
}
