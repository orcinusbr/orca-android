/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.platform.navigation.test.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commitNow
import androidx.fragment.app.testing.EmptyFragmentActivity
import androidx.fragment.app.testing.FragmentFactoryHolderViewModel
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.platform.navigation.NavigationActivity
import kotlin.reflect.full.primaryConstructor

/**
 * Launches the specified [Fragment] in a [NavigationActivity].
 *
 * @param T [Fragment] to be launched.
 * @param instantiation Provides an instance of the [Fragment] to be launched.
 */
inline fun <reified T : Fragment> launchFragmentInNavigationContainer(
  crossinline instantiation: () -> T
): FragmentScenario<T> {
  val context = ApplicationProvider.getApplicationContext<Context>()
  val activityName = ComponentName(context, EmptyFragmentActivity::class.java)
  val activityThemeKey = EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY
  val activityIntent = Intent.makeMainActivity(activityName).putExtra(activityThemeKey, 0)
  val activityScenario = launchActivity<EmptyFragmentActivity>(activityIntent)
  val fragmentFactory = FragmentFactory(instantiation)
  val fragmentJavaClass = T::class.java
  val fragmentScenarioConstructor = checkNotNull(FragmentScenario::class.primaryConstructor)
  val fragmentScenario = fragmentScenarioConstructor.call(fragmentJavaClass, activityScenario)
  activityScenario.onActivity { activity ->
    val fragment = instantiation()
    val fragmentContainerViewID = View.generateViewId()
    val fragmentContainerView =
      FragmentContainerView(activity).apply { id = fragmentContainerViewID }
    activity.setContentView(fragmentContainerView)

    @Suppress("RestrictedApi")
    FragmentFactoryHolderViewModel.getInstance(activity).fragmentFactory = fragmentFactory

    activity.supportFragmentManager.fragmentFactory = fragmentFactory
    activity.supportFragmentManager.commitNow {
      add(fragmentContainerViewID, fragment, "FragmentInNavigationContainer")
    }
  }
  @Suppress("UNCHECKED_CAST") return fragmentScenario as FragmentScenario<T>
}
