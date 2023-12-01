/*
 * Copyright © 2023 Orca
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

package com.jeanbarrossilva.orca.platform.ui.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

internal class FlowExtensionsTests {
  @Test
  fun awaits() {
    val flow = MutableSharedFlow<Int>()
    var awaited = -1
    runTest(@OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()) {
      launch { awaited = flow.await() }
      flow.emit(0)
    }
    assertThat(awaited).isEqualTo(0)
  }

  @Test
  fun awaitsOnStateFlow() {
    val flow = MutableStateFlow(-1)
    var awaited = flow.value
    runTest(@OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()) {
      launch { awaited = flow.await() }
      flow.value = 0
    }
    assertThat(awaited).isEqualTo(0)
  }
}
