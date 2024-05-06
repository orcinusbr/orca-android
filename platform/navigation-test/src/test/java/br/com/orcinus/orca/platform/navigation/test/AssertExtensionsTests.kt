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

package br.com.orcinus.orca.platform.navigation.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.launchFragmentInContainer
import assertk.assertThat
import assertk.assertions.isNotNull
import br.com.orcinus.orca.platform.navigation.test.activity.launchNavigationActivity
import kotlin.test.Test
import org.junit.runner.RunWith
import org.opentest4j.AssertionFailedError
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AssertExtensionsTests {
  class UnexpectedCurrentFragment : Fragment()

  @Test
  fun passesAssertingThatTheSpecifiedFragmentIsTheCurrentOneOfTheActivityAndItIs() {
    launchFragmentInContainer(instantiate = ::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        assertThat(fragment.activity).isNotNull().isAt<_, Fragment>()
      }
    }
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenAssertingThatTheSpecifiedFragmentIsTheCurrentOneOfTheActivityAndNoneHasBeenAdded() {
    launchNavigationActivity().use { scenario ->
      scenario.onActivity { activity: FragmentActivity -> assertThat(activity).isAt<_, Fragment>() }
    }
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenAssertingThatTheSpecifiedFragmentIsTheCurrentOneOfTheActivityAndAnotherOneIs() {
    launchFragmentInContainer(instantiate = ::UnexpectedCurrentFragment).use { scenario ->
      scenario.onFragment { fragment ->
        assertThat(fragment.activity).isNotNull().isAt<_, Fragment>()
      }
    }
  }
}
