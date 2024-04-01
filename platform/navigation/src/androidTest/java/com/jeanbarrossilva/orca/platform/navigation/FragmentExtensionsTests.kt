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

package com.jeanbarrossilva.orca.platform.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSameAs
import org.junit.Test

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

  @Test
  fun getsArgument() {
    launchFragment(bundleOf("argument" to 0), instantiate = ::Fragment).use { scenario ->
      scenario.onFragment { fragment ->
        assertThat(fragment.argument<Int>("argument").value).isEqualTo(0)
      }
    }
  }
}
