package com.jeanbarrossilva.orca.platform.theme.extensions

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

/**
 * Adds a border stroke, shaped by the given [shape], only when in the light theme.
 *
 * @param shape [Shape] of the stroke to be added.
 **/
fun Modifier.border(shape: Shape): Modifier {
    return composed {
        if (isSystemInDarkTheme()) {
            this
        } else {
            border(2.dp, OrcaTheme.colors.placeholder, shape)
        }
    }
}
