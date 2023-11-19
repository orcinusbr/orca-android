package com.jeanbarrossilva.orca.platform.theme.autos.borders

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.autos.borders.Border
import com.jeanbarrossilva.orca.platform.theme.autos.colors.asColor

/** [BorderStroke] version of this [Border]. */
val Border.asBorderStroke
  get() = BorderStroke(width.dp, color.asColor)
