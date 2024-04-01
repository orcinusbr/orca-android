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

package com.jeanbarrossilva.orca.platform.navigation.test.activity

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.platform.navigation.Navigator

/**
 * Launches a [FragmentActivity] whose content is a [FragmentContainerView], which allows for its
 * [Navigator] to be obtained.
 */
fun launchNavigationActivity(): ActivityScenario<FragmentActivity> {
  @Suppress("UNCHECKED_CAST")
  return launchActivity<NavigationActivity>().onActivity(FragmentActivity::makeNavigable)
    as ActivityScenario<FragmentActivity>
}
