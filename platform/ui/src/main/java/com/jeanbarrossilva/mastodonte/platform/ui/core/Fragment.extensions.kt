package com.jeanbarrossilva.mastodonte.platform.ui.core

import android.app.Application
import androidx.fragment.app.Fragment

/**
 * [Application] to which this [Fragment] is attached.
 *
 * @throws IllegalStateException If it's not attached.
 **/
val Fragment.application
    get() = activity?.application ?: throw IllegalStateException(
        "Fragment $this not attached to an Application."
    )

/**
 * Gets the argument put with the given [key] lazily.
 *
 * @param key Key to which the argument is associated.
 * @throws ClassCastException If the argument is present but isn't a [T].
 **/
fun <T> Fragment.argument(key: String): Lazy<T> {
    return lazy {
        @Suppress("DEPRECATION", "UNCHECKED_CAST")
        requireArguments().get(key) as T
    }
}
