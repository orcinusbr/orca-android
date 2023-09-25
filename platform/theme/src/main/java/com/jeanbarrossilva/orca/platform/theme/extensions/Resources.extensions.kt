package com.jeanbarrossilva.orca.platform.theme.extensions

import android.content.res.Resources
import androidx.annotation.FloatRange
import androidx.annotation.FractionRes

/**
 * Loads the fraction associated with the given resource [id].
 *
 * @param id ID of the fraction resource.
 **/
@FloatRange(from = 0.0, to = 1.0)
internal fun Resources.getFraction(@FractionRes id: Int): Float? {
    return try {
        getFraction(id, 1, 1)
    } catch (_: Resources.NotFoundException) {
        null
    }
}
