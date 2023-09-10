package com.jeanbarrossilva.orca.platform.theme.extensions

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/** An empty [ImageVector]. **/
internal val ImageVector.Companion.Empty
    get() = ImageVector
        .Builder(
            name = "ImageVector.Empty",
            defaultWidth = 0.dp,
            defaultHeight = 0.dp,
            viewportWidth = 0f,
            viewportHeight = 0f
        )
        .build()
