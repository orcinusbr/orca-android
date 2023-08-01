package com.jeanbarrossilva.orca.platform.theme.extensions

import android.content.res.TypedArray
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

/**
 * Loads the [Color] from the given attribute.
 *
 * @param id Attribute ID of the [Color] to be loaded.
 **/
@Composable
internal fun colorAttribute(@AttrRes id: Int): Color {
    val theme = LocalContext.current.theme
    val typedValue = TypedValue()
    theme.resolveAttribute(id, typedValue, false)
    val typedArray: TypedArray = theme.obtainStyledAttributes(typedValue.data, intArrayOf(id))
    val defaultValue = Color.Unspecified.toArgb()
    val value = typedArray.getColor(0, defaultValue)
    return Color(value)
}
