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

package br.com.orcinus.orca.platform.navigation.destination

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import assertk.assertThat
import assertk.assertions.isSameAs
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class DestinationFragmentTests {
  class ToAttachDestinationFragment : DestinationFragment(::id) {
    companion object {
      val id = generateID()
    }
  }

  @Test
  fun isNotIdentifiedWhenUnattached() {
    assertThat(ToAttachDestinationFragment().getId()).isSameAs(DestinationFragment.NO_ID)
  }

  @Test
  fun isIdentifiedWhenAttached() {
    launchFragment(instantiate = ::ToAttachDestinationFragment).use { scenario ->
      scenario.onFragment { fragment ->
        assertThat(fragment.getId()).isSameAs(ToAttachDestinationFragment.id)
      }
    }
  }

  @Test
  fun isNotIdentifiedWhenDetached() {
    val fragment = ToAttachDestinationFragment()
    launchFragment { fragment }
      .use {
        it.moveToState(Lifecycle.State.DESTROYED)
        assertThat(fragment.getId()).isSameAs(DestinationFragment.NO_ID)
      }
  }
}
