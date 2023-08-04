package com.jeanbarrossilva.orca.platform.ui.core

import android.app.Activity
import android.content.Context

/**
 * Creates an [ActivityStarter] for the [Activity], from which it can be set up and started.
 *
 * @param T [Activity] whose start-up may be configured.
 * @see ActivityStarter.start
 **/
inline fun <reified T : Activity> Context.on(): ActivityStarter<T> {
    return ActivityStarter(this, T::class)
}
