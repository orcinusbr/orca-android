package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView

/** [FragmentActivity] through which [Navigator]-based navigation can be performed. **/
open class NavigationActivity : FragmentActivity() {
    /** Root [View] of this [NavigationActivity]. **/
    private val view by lazy {
        requireViewById<ViewGroup>(android.R.id.content)
    }

    /**
     * [Navigator] through which navigation can be performed.
     *
     * **NOTE**: Because the [FragmentContainerView] that this [NavigationActivity] holds needs to
     * have an ID for the [Navigator] to work properly, one is automatically generated and assigned
     * to it if it doesn't already have one.
     **/
    val navigator
        get() = view
            .get<FragmentContainerView>()
            .apply { if (id == View.NO_ID) id = View.generateViewId() }
            .let { Navigator(supportFragmentManager, it.id) }
}
