/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.platform.navigation

import androidx.fragment.app.Fragment
import androidx.test.core.app.launchActivity
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import br.com.orcinus.orca.platform.navigation.duplication.disallowingDuplication
import br.com.orcinus.orca.platform.navigation.test.isAt
import br.com.orcinus.orca.platform.navigation.transition.closing
import br.com.orcinus.orca.platform.navigation.transition.opening
import br.com.orcinus.orca.platform.navigation.transition.suddenly
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class NavigatorTests {
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
    launchActivity<NavigationActivity>().use { scenario ->
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
    launchActivity<NavigationActivity>().use { scenario ->
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
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.navigator.navigate(closing()) {
          to(FirstDestinationFragment.ROUTE, NavigatorTests::FirstDestinationFragment)
        }
        assertThat(activity).isAt(FirstDestinationFragment.ROUTE)
      }
    }
  }

  @Test
  fun navigatesTwiceWhenDuplicationIsAllowed() {
    launchActivity<NavigationActivity>().use { scenario ->
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
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        repeat(2) {
          activity.navigator.navigate(suddenly(), disallowingDuplication()) {
            to(FirstDestinationFragment.ROUTE, NavigatorTests::FirstDestinationFragment)
          }
        }
        assertThat(activity.supportFragmentManager.fragments.size).isEqualTo(1)
      }
    }
  }

  @Test
  fun navigatesSequentially() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        with(activity.navigator) {
          navigate(suddenly()) { to(FirstDestinationFragment.ROUTE, ::FirstDestinationFragment) }
          navigate(suddenly()) { to(SecondDestinationFragment.ROUTE, ::SecondDestinationFragment) }
        }
        assertThat(activity).isAt(SecondDestinationFragment.ROUTE)
      }
    }
  }

  @Test
  fun listensToDestinationChange() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        lateinit var destination: Navigator.Navigation.Destination<*>
        activity.navigator.addOnDestinationChangeListener { destination = it }
        activity.navigator.navigate(suddenly()) {
          to(FirstDestinationFragment.ROUTE, ::FirstDestinationFragment)
        }
        assertThat(destination)
          .isInstanceOf<Navigator.Navigation.Destination<FirstDestinationFragment>>()
      }
    }
  }

  @Test
  fun removesOnDestinationChangeListener() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        var hasListenerBeenNotified = false
        val listener =
          Navigator.Navigation.Destination.OnChangeListener { hasListenerBeenNotified = true }
        activity.navigator.addOnDestinationChangeListener(listener)
        activity.navigator.removeOnDestinationChangeListener(listener)
        activity.navigator.navigate(suddenly()) {
          to(FirstDestinationFragment.ROUTE, ::FirstDestinationFragment)
        }
        assertThat(hasListenerBeenNotified).isFalse()
      }
    }
  }
}
