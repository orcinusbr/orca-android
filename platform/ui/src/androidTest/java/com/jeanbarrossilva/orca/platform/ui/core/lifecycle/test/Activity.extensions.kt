package com.jeanbarrossilva.orca.platform.ui.core.lifecycle.test

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Runs the [action] when this [Activity] is destroyed.
 *
 * @param action Operation to be performed when this [Activity] is destroyed.
 **/
internal fun <T : Activity> T.doOnDestroy(action: T.() -> Unit) {
    registerActivityLifecycleCallbacks(
        object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                @Suppress("UNCHECKED_CAST")
                (activity as T).action()

                unregisterActivityLifecycleCallbacks(this)
            }
        }
    )
}
