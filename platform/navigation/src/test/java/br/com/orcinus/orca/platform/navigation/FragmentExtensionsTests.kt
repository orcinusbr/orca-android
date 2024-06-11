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

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.testing.launchFragment
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.isSameAs
import br.com.orcinus.orca.platform.navigation.test.activity.makeNavigable
import br.com.orcinus.orca.platform.navigation.test.fragment.launchFragmentInNavigationContainer
import br.com.orcinus.orca.platform.navigation.transition.suddenly
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class FragmentExtensionsTests {
  @Test(expected = IllegalStateException::class)
  fun throwsWhenGettingApplicationWhileDetached() {
    lateinit var fragment: Fragment
    launchFragment(instantiate = ::Fragment).use { scenario ->
      scenario.onFragment { fragment = it }
    }
    fragment.application
  }

  @Test
  fun getsApplicationFromActivity() {
    launchFragment(instantiate = ::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        assertThat(fragment.application).isSameAs(fragment.activity?.application)
      }
    }
  }

  @Test(expected = IllegalStateException::class)
  fun throwsWhenNavigatorGetterIsCalledOnFragmentAttachedToAnActivityWithoutFragmentContainerView() {
    launchFragment(instantiate = ::Fragment).use { scenario ->
      scenario.onFragment { fragment -> fragment.navigator }
    }
  }

  @Test(expected = IllegalStateException::class)
  fun throwsWhenNavigatorGetterIsCalledOnFragmentAttachedToAnActivityWithFragmentContainerViewAndAgainWithoutIt() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        val activity = fragment.requireActivity().apply(FragmentActivity::makeNavigable)
        fragment.navigator
        activity.content.removeAllViews()
        fragment.navigator
      }
    }
  }

  @Test
  fun identifiesFragmentContainerViewIfNotIdentifiedWhenNavigatorGetterIsCalled() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        val context = fragment.requireContext()
        val activity =
          fragment.requireActivity().apply { setContentView(FragmentContainerView(context)) }
        fragment.navigator
        assertThat(activity.content.get<FragmentContainerView>(isInclusive = false).id)
          .isNotEqualTo(View.NO_ID)
      }
    }
  }

  @Test
  fun returnsNavigatorWhenItsGetterIsCalledOnFragmentAttachedToAnActivityWithFragmentContainerView() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        fragment.requireActivity().makeNavigable()
        fragment.navigator
      }
    }
  }

  @Test
  fun getsParentNavigator() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { firstFragment ->
        val secondFragment = Fragment()
        firstFragment.navigator.navigate(suddenly()) { secondFragment }
        assertThat(secondFragment.parentNavigator).isSameAs(firstFragment.navigator)
      }
    }
  }

  @Test
  fun parentNavigatorIsThatOfTheFragmentItselfWhenItIsTheOnlyOne() {
    launchFragmentInNavigationContainer(::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        assertThat(fragment.parentNavigator).isSameAs(fragment.navigator)
      }
    }
  }

  @Test
  fun getsArgument() {
    launchFragment(bundleOf("argument" to 0), instantiate = ::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        assertThat(fragment.argument<Int>("argument").value).isEqualTo(0)
      }
    }
  }
}
