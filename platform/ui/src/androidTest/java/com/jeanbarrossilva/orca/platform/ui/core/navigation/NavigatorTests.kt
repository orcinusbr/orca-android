/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.platform.ui.core.navigation.duplication.disallowingDuplication
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.closing
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.suddenly
import com.jeanbarrossilva.orca.platform.ui.test.assertIsAtFragment
import org.junit.Assert.assertEquals
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
          to(FirstDestinationFragment.ROUTE, ::FirstDestinationFragment)
        }
        assertIsAtFragment(activity, FirstDestinationFragment.ROUTE)
      }
    }
  }

  @Test
  fun navigatesWithOpeningTransition() {
    launchActivity<TestNavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.navigator.navigate(opening()) {
          to(FirstDestinationFragment.ROUTE, ::FirstDestinationFragment)
        }
        assertIsAtFragment(activity, FirstDestinationFragment.ROUTE)
      }
    }
  }

  @Test
  fun navigatesWithClosingTransition() {
    launchActivity<TestNavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.navigator.navigate(closing()) {
          to(FirstDestinationFragment.ROUTE, ::FirstDestinationFragment)
        }
        assertIsAtFragment(activity, FirstDestinationFragment.ROUTE)
      }
    }
  }

  @Test
  fun navigatesTwiceWhenDuplicationIsAllowed() {
    launchActivity<TestNavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        repeat(2) {
          activity.navigator.navigate(suddenly()) {
            to(FirstDestinationFragment.ROUTE, ::FirstDestinationFragment)
          }
        }
        assertEquals(2, activity.supportFragmentManager.fragments.size)
      }
    }
  }

  @Test
  fun navigatesOnceWhenDuplicationIsDisallowed() {
    launchActivity<TestNavigationActivity>().use { scenario ->
      scenario.onActivity { activity ->
        repeat(2) {
          activity.navigator.navigate(suddenly(), disallowingDuplication()) {
            to(FirstDestinationFragment.ROUTE, ::FirstDestinationFragment)
          }
        }
        assertEquals(1, activity.supportFragmentManager.fragments.size)
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
        assertIsAtFragment(activity, SecondDestinationFragment.ROUTE)
      }
    }
  }
}
