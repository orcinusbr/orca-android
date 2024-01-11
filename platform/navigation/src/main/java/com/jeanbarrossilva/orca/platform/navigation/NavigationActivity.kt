/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.navigation

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
