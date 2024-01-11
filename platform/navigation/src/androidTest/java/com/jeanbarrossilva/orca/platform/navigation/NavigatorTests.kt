/*
 * Copyright © 2023-2024 Orca
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

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.test.core.app.launchActivity
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.platform.navigation.test.isAt
import com.jeanbarrossilva.orca.platform.navigation.transition.opening
import com.jeanbarrossilva.orca.platform.navigation.transition.suddenly
import org.junit.Test

internal class NavigatorTests {
  class TestNavigationActivity : NavigationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(FragmentContainerView(this))
    }
  }

  class FirstDestinationFragment : Fragment() {
    companion object {
      const val ROUTE = "first-destination"
    }
  }

  class SecondDestinationFragment : Fragment() {
    companion object {
      const val ROUTE = "second-destination"
    }
  }

  @Test
  fun navigatesSuddenly() {
    launchActivity<TestNavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.navigator.navigate(suddenly()) {
          to(FirstDestinationFragment.ROUTE, NavigatorTests::FirstDestinationFragment)
        }
        assertThat(activity).isAt(FirstDestinationFragment.ROUTE)
      }
    }
  }

  @Test
  fun navigatesWithOpeningTransition() {
    launchActivity<TestNavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.navigator.navigate(opening()) {
          to(FirstDestinationFragment.ROUTE, NavigatorTests::FirstDestinationFragment)
        }
        assertThat(activity).isAt(FirstDestinationFragment.ROUTE)
      }
    }
  }

  @Test
  fun navigatesWithClosingTransition() {
    launchActivity<TestNavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.navigator.navigate(
          com.jeanbarrossilva.orca.platform.navigation.transition.closing()
        ) {
          to(FirstDestinationFragment.ROUTE, NavigatorTests::FirstDestinationFragment)
        }
        assertThat(activity).isAt(FirstDestinationFragment.ROUTE)
      }
    }
  }

  @Test
  fun navigatesTwiceWhenDuplicationIsAllowed() {
    launchActivity<TestNavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        repeat(2) {
          activity.navigator.navigate(suddenly()) {
            to(FirstDestinationFragment.ROUTE, NavigatorTests::FirstDestinationFragment)
          }
        }
        assertThat(activity.supportFragmentManager.fragments.size).isEqualTo(2)
      }
    }
  }

  @Test
  fun navigatesOnceWhenDuplicationIsDisallowed() {
    launchActivity<TestNavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        repeat(2) {
          activity.navigator.navigate(
            suddenly(),
            com.jeanbarrossilva.orca.platform.navigation.duplication.disallowingDuplication()
          ) {
            to(FirstDestinationFragment.ROUTE, NavigatorTests::FirstDestinationFragment)
          }
        }
        assertThat(activity.supportFragmentManager.fragments.size).isEqualTo(1)
      }
    }
  }

  @Test
  fun navigatesSequentially() {
    launchActivity<TestNavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        with(activity.navigator) {
          navigate(suddenly()) { to(FirstDestinationFragment.ROUTE, ::FirstDestinationFragment) }
          navigate(suddenly()) { to(SecondDestinationFragment.ROUTE, ::SecondDestinationFragment) }
        }
        assertThat(activity).isAt(SecondDestinationFragment.ROUTE)
      }
    }
  }
}
