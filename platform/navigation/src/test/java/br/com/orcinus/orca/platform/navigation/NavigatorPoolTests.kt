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

package br.com.orcinus.orca.platform.navigation

import androidx.fragment.app.Fragment
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isFalse
import assertk.assertions.isSameAs
import br.com.orcinus.orca.platform.navigation.test.fragment.launchFragmentInNavigationContainer
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class NavigatorPoolTests {
  @Test
  fun remembers() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        val navigator = Navigator.Pool.get(fragment)
        assertThat(Navigator.Pool.get(fragment)).isSameAs(navigator)
      }
    }
  }

  @Test
  fun removesNavigatorAfterActivityToWhichTheFragmentIsAttachedIsDestroyed() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        Navigator.Pool.get(fragment)
        scenario.recreate()
        assertThat(fragment in Navigator.Pool).isFalse()
      }
    }
  }

  @Test
  fun removesOnNavigationListenersFromNavigatorAfterTheActivityToWhichTheFragmentIsAttachedIsDestroyed() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        val navigator = Navigator.Pool.get(fragment)
        fragment.requireActivity().finish()
        assertThat(navigator.onNavigationListeners).isEmpty()
      }
    }
  }
}
