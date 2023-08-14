package com.jeanbarrossilva.orca.platform.theme.configuration.colors.contrast

import androidx.compose.ui.graphics.Color

/**
 * Creates a [Contrast] with the two contrasting [Color]s, this one and the [contentColor].
 *
 * @param contentColor [Color] to color the content with.
 **/
internal infix fun Color.and(contentColor: Color): Contrast {
    return Contrast(this, contentColor)
}
