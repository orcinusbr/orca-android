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

package com.jeanbarrossilva.orca.composite.timeline.stat.details

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class FlowExtensionsTests {
  @Test
  fun doesNotEmitBeforePrimaryEmits() {
    var hasEmitted: Boolean? = null
    runTest { flowOf(0).dependOn(emptyFlow<Any>()).onEmpty { hasEmitted = false }.collect() }
    assertThat(hasEmitted).isEqualTo(false)
  }

  @Test
  fun emitsBothValuesOfPrimaryAndDependantFlowsWhenDependantEmitsBeforePrimary() {
    val dependant = MutableSharedFlow<Int>(replay = 1)
    val primary = MutableSharedFlow<Int>()
    runTest {
      dependant.dependOn(primary).test {
        dependant.emit(1)
        primary.emit(0)
        assertThat(awaitItem()).isEqualTo(0 to 1)
      }
    }
  }
}
