package com.jeanbarrossilva.orca.platform.theme.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color

/**
 * Gets a [Color] that's identified as [id].
 *
 * @param context [Context] from which the [Color] will be obtained.
 * @param id Resource ID of the [Color].
 **/
internal fun Color.Companion.of(context: Context, @ColorRes id: Int): Color {
    val value = context.getColor(id)
    return Color(value)
}
