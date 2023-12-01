/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.platform.ui.core.lifecycle.test

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
