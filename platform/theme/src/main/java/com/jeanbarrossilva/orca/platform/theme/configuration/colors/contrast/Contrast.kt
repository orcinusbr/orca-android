package com.jeanbarrossilva.orca.platform.theme.configuration.colors.contrast

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Holds two contrasting [Color]s, one for a background and the other for the content over it.
 *
 * @param container [Color] to color a container with.
 * @param content [Color] to color the content with.
 **/
@Immutable
data class Contrast internal constructor(val container: Color, val content: Color) {
    companion object {
        /** [Contrast] with [Color.Unspecified] values. **/
        val Unspecified = Color.Unspecified and Color.Unspecified
    }
}
