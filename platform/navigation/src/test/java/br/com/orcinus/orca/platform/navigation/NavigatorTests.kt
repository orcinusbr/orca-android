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

import androidx.test.core.app.launchActivity
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isSameAs
import br.com.orcinus.orca.platform.navigation.destination.DestinationFragment
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
  class FirstDestinationFragment : DestinationFragment({ id }) {
    companion object {
      val id = generateID()
    }
  }

  class SecondDestinationFragment : DestinationFragment({ id }) {
    companion object {
      val id = generateID()
    }
  }

  @Test
  fun navigatesSuddenly() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        activity.navigator.navigateToDestinationFragment(suddenly(), FirstDestinationFragment())
        assertThat(activity).isAt<_, FirstDestinationFragment>(FirstDestinationFragment.id)
      }
    }
  }

  @Test
  fun navigatesWithOpeningTransition() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        activity.navigator.navigateToDestinationFragment(opening(), FirstDestinationFragment())
        assertThat(activity).isAt<_, FirstDestinationFragment>(FirstDestinationFragment.id)
      }
    }
  }

  @Test
  fun navigatesWithClosingTransition() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        activity.navigator.navigateToDestinationFragment(closing(), FirstDestinationFragment())
        assertThat(activity).isAt<_, FirstDestinationFragment>(FirstDestinationFragment.id)
      }
    }
  }

  @Test
  fun navigatesTwiceWhenDuplicationIsAllowed() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        repeat(2) {
          activity.navigator.navigateToDestinationFragment(suddenly(), FirstDestinationFragment())
        }
        assertThat(activity.supportFragmentManager.fragments.size).isEqualTo(2)
      }
    }
  }

  @Test
  fun navigatesOnceWhenDuplicationIsDisallowed() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        repeat(2) {
          activity.navigator.navigateToDestinationFragment(
            suddenly(),
            disallowingDuplication(),
            FirstDestinationFragment()
          )
        }
        assertThat(activity.supportFragmentManager.fragments.size).isEqualTo(1)
      }
    }
  }

  @Test
  fun navigatesSequentially() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        with(activity.navigator) {
          navigateToDestinationFragment(suddenly(), FirstDestinationFragment())
          navigateToDestinationFragment(suddenly(), SecondDestinationFragment())
        }
        assertThat(activity).isAt<_, SecondDestinationFragment>(SecondDestinationFragment.id)
      }
    }
  }

  @Test
  fun fragmentIdentifierRemainsBeingTheLazilySpecifiedOneAfterItIsHostedByAContainer() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        val fragment = FirstDestinationFragment()
        activity.navigator.navigateToDestinationFragment(suddenly(), fragment)
        assertThat(fragment.getId()).isSameAs(FirstDestinationFragment.id)
      }
    }
  }

  @Test
  fun addsOnNavigationListener() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        lateinit var fragment: DestinationFragment
        activity.navigator.addOnNavigationListener { fragment = it as DestinationFragment }
        activity.navigator.navigateToDestinationFragment(suddenly(), FirstDestinationFragment())
        assertThat(fragment).isInstanceOf<FirstDestinationFragment>()
      }
    }
  }

  @Test
  fun removesOnNavigationListener() {
    launchActivity<NavigationActivity>().use { scenario ->
      scenario.onActivity { activity: NavigationActivity ->
        var hasListenerBeenNotified = false
        val listener = Navigator.OnNavigationListener { hasListenerBeenNotified = true }
        activity.navigator.addOnNavigationListener(listener)
        activity.navigator.removeOnNavigationListener(listener)
        activity.navigator.navigateToDestinationFragment(suddenly(), FirstDestinationFragment())
        assertThat(hasListenerBeenNotified).isFalse()
      }
    }
  }
}
