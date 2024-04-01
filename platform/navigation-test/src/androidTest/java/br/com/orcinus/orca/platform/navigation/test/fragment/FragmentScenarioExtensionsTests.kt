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

package br.com.orcinus.orca.platform.navigation.test.fragment

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import assertk.assertThat
import assertk.assertions.isSameAs
import br.com.orcinus.orca.platform.navigation.navigator
import kotlin.test.Test

internal class FragmentScenarioExtensionsTests {
  @Test
  fun launchesFragmentInNavigationContainerWithArguments() {
    val args = bundleOf("id" to 0)
    launchFragmentInNavigationContainer(args, ::Fragment).use { scenario ->
      scenario.onFragment { fragment -> assertThat(fragment.arguments).isSameAs(args) }
    }
  }

  @Test
  fun getsNavigatorFromContainerActivity() {
    launchFragmentInNavigationContainer(instantiate = ::Fragment).use { scenario ->
      scenario.onFragment { fragment -> fragment.requireActivity().navigator }
    }
  }
}
