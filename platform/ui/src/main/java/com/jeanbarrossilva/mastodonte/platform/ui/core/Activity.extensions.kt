package com.jeanbarrossilva.mastodonte.platform.ui.core

import android.app.Activity
import android.content.Intent

/**
 * Gets the argument put to this [Activity]'s [intent][Activity.getIntent]'s
 * [extras][Intent.getExtras] with the given [key] lazily.
 *
 * @param key Key to which the argument is associated.
 * @throws ClassCastException If the argument is present but isn't a [T].
 **/
fun <T> Activity.argument(key: String): Lazy<T> {
    return lazy {
        @Suppress("DEPRECATION", "UNCHECKED_CAST")
        intent.requireExtras()[key] as T
    }
}
