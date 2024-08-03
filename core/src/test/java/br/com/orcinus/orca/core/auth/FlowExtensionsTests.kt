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

package br.com.orcinus.orca.core.auth

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import kotlin.test.Test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class FlowExtensionsTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenWindowingWithANegativeSize() {
    flowOf(0, 1).windowed(-1)
  }

  @Test
  fun returnsAnEmptyFlowWhenWindowingWithAZeroedCount() {
    runTest {
      flowOf(0, 1).windowed(0).test(validate = TurbineTestContext<List<Int>>::awaitComplete)
    }
  }

  @Test
  fun windows() {
    runTest {
      flowOf(0, 2, 4, 16, 256).windowed(2).test {
        assertThat(awaitItem()).containsExactly(0, 2)
        assertThat(awaitItem()).containsExactly(4, 16)
        awaitComplete()
      }
    }
  }
}
