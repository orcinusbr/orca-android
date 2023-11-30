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
