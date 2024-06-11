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
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import br.com.orcinus.orca.platform.navigation.duplication.disallowingDuplication
import br.com.orcinus.orca.platform.navigation.test.fragment.launchFragmentInNavigationContainer
import br.com.orcinus.orca.platform.navigation.test.isAt
import br.com.orcinus.orca.platform.navigation.transition.closing
import br.com.orcinus.orca.platform.navigation.transition.opening
import br.com.orcinus.orca.platform.navigation.transition.suddenly
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class NavigatorTests {
  class FirstFragment : Fragment()

  class SecondFragment : Fragment()

  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenNavigatingUnsafelyToAFragmentDistinctFromSpecifiedOne() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        fragment.navigator.navigateOrThrow(
          suddenly(),
          disallowingDuplication(),
          FirstFragment::class,
          ::SecondFragment
        )
      }
    }
  }

  @Test
  fun navigatesSuddenly() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        fragment.navigator.navigate(suddenly(), ::FirstFragment)
        assertThat(fragment.requireActivity()).isAt<_, FirstFragment>()
      }
    }
  }

  @Test
  fun navigatesWithOpeningTransition() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        fragment.navigator.navigate(opening(), ::FirstFragment)
        assertThat(fragment.requireActivity()).isAt<_, FirstFragment>()
      }
    }
  }

  @Test
  fun navigatesWithClosingTransition() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        fragment.navigator.navigate(closing(), ::FirstFragment)
        assertThat(fragment.requireActivity()).isAt<_, FirstFragment>()
      }
    }
  }

  @Test
  fun navigatesTwiceWhenDuplicationIsAllowed() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        repeat(2) { fragment.navigator.navigate(suddenly(), ::FirstFragment) }
        assertThat(fragment.parentFragmentManager.fragments.size).isEqualTo(2)
      }
    }
  }

  @Test
  fun navigatesOnceWhenDuplicationIsDisallowed() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        repeat(2) {
          fragment.navigator.navigate(suddenly(), disallowingDuplication(), ::FirstFragment)
        }
        assertThat(fragment.parentFragmentManager.fragments.size).isEqualTo(2)
      }
    }
  }

  @Test
  fun navigatesSequentially() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        with(fragment.navigator) {
          navigate(suddenly(), ::FirstFragment)
          navigate(suddenly(), ::SecondFragment)
        }
        assertThat(fragment.requireActivity()).isAt<_, SecondFragment>()
      }
    }
  }

  @Test
  fun addsOnNavigationListener() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        lateinit var destination: Fragment
        fragment.navigator.addOnNavigationListener { destination = it }
        fragment.navigator.navigate(suddenly(), ::FirstFragment)
        assertThat(destination).isInstanceOf<FirstFragment>()
      }
    }
  }

  @Test
  fun removesOnNavigationListener() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        var hasListenerBeenNotified = false
        val listener = Navigator.OnNavigationListener { hasListenerBeenNotified = true }
        fragment.navigator.addOnNavigationListener(listener)
        fragment.navigator.removeOnNavigationListener(listener)
        fragment.navigator.navigate(suddenly(), ::FirstFragment)
        assertThat(hasListenerBeenNotified).isFalse()
      }
    }
  }
}
