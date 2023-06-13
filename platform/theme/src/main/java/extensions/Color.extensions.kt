package com.jeanbarrossilva.mastodonte.platform.theme.extensions

import androidx.annotation.AttrRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.color.MaterialColors

/**
 * Loads the [Color] from the given attribute.
 *
 * @param id Attribute ID of the [Color] to be loaded.
 **/
@Composable
internal fun colorAttribute(@AttrRes id: Int): Color {
    val value = MaterialColors.getColor(LocalContext.current, id, 0)
    return Color(value)
}
