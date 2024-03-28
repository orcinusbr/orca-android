/*
 * Copyright © 2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.navigation

import androidx.fragment.app.FragmentManager
import assertk.assertThat
import assertk.assertions.isNotSameAs
import assertk.assertions.isSameAs
import io.mockk.mockk
import kotlin.test.Test
import org.junit.After

internal class NavigatorPoolTests {
  @After
  fun tearDown() {
    Navigator.Pool.clear()
  }

  @Test
  fun retrievesNavigatorWhenGettingItSubsequently() {
    mockk<FragmentManager> {
      val navigator = Navigator.Pool.get(this, containerID = 0)
      assertThat(Navigator.Pool.get(this, containerID = 0)).isSameAs(navigator)
    }
  }

  @Test
  fun getsDistinctNavigatorsForDifferentContainerIds() {
    mockk<FragmentManager> {
      assertThat(Navigator.Pool.get(this, containerID = 0))
        .isNotSameAs(Navigator.Pool.get(this, containerID = 1))
    }
  }

  @Test
  fun clears() {
    mockk<FragmentManager> {
      val navigator = Navigator.Pool.get(this, containerID = 0)
      Navigator.Pool.clear()
      assertThat(Navigator.Pool.get(this, containerID = 0)).isNotSameAs(navigator)
    }
  }
}
