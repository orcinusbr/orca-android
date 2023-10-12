package com.jeanbarrossilva.orca.platform.launchable

import android.app.Application
import android.content.Context

/**
 * Allows the structure implementing this interface to be marked as launched and provides a [count]
 * that indicates how many times that's happened.
 *
 * @see markAsLaunched
 */
@Suppress("SpellCheckingInspection")
interface Launchable {
  /** Returns the number of times this has been launched. */
  fun count(): Int

  /** Marks this as launched. */
  fun markAsLaunched()
}

/**
 * Whether this is the first time the application has ever been launched by the user.
 *
 * @throws IllegalStateException If the running [Application] instance isn't [Launchable].
 */
val Context.isFirstLaunch: Boolean
  get() {
    val launchable =
      applicationContext as? Launchable
        ?: throw IllegalStateException(
          "Cannot check first launch because the Application is not Launchable."
        )
    return launchable.count() == 1
  }
