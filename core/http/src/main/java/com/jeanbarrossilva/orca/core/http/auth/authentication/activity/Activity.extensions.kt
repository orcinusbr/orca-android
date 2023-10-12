package com.jeanbarrossilva.orca.core.http.auth.authentication.activity

import android.app.Activity
import android.content.Intent

/**
 * Gets this [Activity]'s [Intent][Intent] extra put with the given [key] lazily.
 *
 * @param key Key to which the extra is associated.
 * @throws ClassCastException If the extra is present but isn't a [T].
 */
internal inline fun <reified T> Activity.extra(key: String): Lazy<T> {
  return lazy {
    @Suppress("DEPRECATION")
    intent?.extras?.get(key) as T
  }
}
