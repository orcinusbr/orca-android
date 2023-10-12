package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView

/** [FragmentActivity] through which [Navigator]-based navigation can be performed. */
open class NavigationActivity : FragmentActivity() {
  /**
   * [Navigator] through which navigation can be performed.
   *
   * **NOTE**: Because the [FragmentContainerView] that this [NavigationActivity] holds needs to
   * have an ID for the [Navigator] to work properly, one is automatically generated and assigned to
   * it if it doesn't already have one.
   *
   * @throws IllegalStateException If a [FragmentContainerView] is not found within the [View] tree.
   */
  val navigator
    get() =
      requireViewById<ViewGroup>(android.R.id.content)
        .get<FragmentContainerView>(isInclusive = false)
        .also(View::identify)
        .let { Navigator(supportFragmentManager, it.id) }
}
