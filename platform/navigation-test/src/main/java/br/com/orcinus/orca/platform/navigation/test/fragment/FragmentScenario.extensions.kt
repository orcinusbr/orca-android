/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.platform.navigation.test.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import br.com.orcinus.orca.platform.navigation.Navigator
import br.com.orcinus.orca.platform.navigation.navigator
import br.com.orcinus.orca.platform.navigation.test.activity.makeNavigable

/**
 * Launches the specified [Fragment] in a [FragmentActivity] whose content is a
 * [FragmentContainerView], which, in turn, allows for its [Navigator] to be obtained.
 *
 * @param T [Fragment] to be launched.
 * @param instantiate Creates an instance of the [Fragment] to be launched.
 * @see navigator
 */
inline fun <reified T : Fragment> launchFragmentInNavigationContainer(
  crossinline instantiate: () -> T
): FragmentScenario<T> {
  return launchFragmentInContainer(instantiate = instantiate).onFragment {
    it.activity?.makeNavigable()
  }
}
