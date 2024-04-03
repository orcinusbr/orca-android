/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.platform.starter.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Runs the [action] when this [Activity] is destroyed.
 *
 * @param action Operation to be performed when this [Activity] is destroyed.
 */
internal fun <T : Activity> T.doOnDestroy(action: T.() -> Unit) {
  registerActivityLifecycleCallbacks(
    object : Application.ActivityLifecycleCallbacks {
      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

      override fun onActivityStarted(activity: Activity) {}

      override fun onActivityResumed(activity: Activity) {}

      override fun onActivityPaused(activity: Activity) {}

      override fun onActivityStopped(activity: Activity) {}

      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

      override fun onActivityDestroyed(activity: Activity) {
        @Suppress("UNCHECKED_CAST") (activity as T).action()
        unregisterActivityLifecycleCallbacks(this)
      }
    }
  )
}
