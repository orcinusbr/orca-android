package com.jeanbarrossilva.orca.platform.ui.core

import android.app.Activity
import android.view.View
import android.view.ViewGroup

/** [ViewGroup] in which this [Activity]'s [View] is. */
val Activity.content
    get() = requireViewById<ViewGroup>(android.R.id.content)
