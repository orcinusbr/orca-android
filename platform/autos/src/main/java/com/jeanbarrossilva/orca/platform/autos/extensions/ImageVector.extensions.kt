package com.jeanbarrossilva.orca.platform.autos.extensions

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/** An empty [ImageVector]. */
internal val ImageVector.Companion.Empty
  get() =
    ImageVector.Builder(
        name = "ImageVector.Empty",
        defaultWidth = 1.dp,
        defaultHeight = 1.dp,
        viewportWidth = 1f,
        viewportHeight = 1f
      )
      .build()
