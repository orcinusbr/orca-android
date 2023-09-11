package com.jeanbarrossilva.orca.platform.theme.extensions

import android.content.res.Resources
import androidx.annotation.FloatRange
import androidx.annotation.FractionRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Loads the fraction associated with the given resource [id].
 *
 * @param id ID of the fraction resource.
 **/
@Composable
@FloatRange(from = 0.0, to = 1.0)
internal fun fractionResource(@FractionRes id: Int): Float? {
    val context = LocalContext.current
    return try {
        context.resources.getFraction(id, 1, 1)
    } catch (_: Resources.NotFoundException) {
        null
    }
}
