package com.jeanbarrossilva.orca.core.feed

import app.cash.turbine.test
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class FlowExtensionsTests {
  @Test
  fun filtersEach() {
    runTest {
      flowOf(listOf(0, 1, 2))
        .filterEach { it % 2 == 0 }
        .test {
          assertEquals(listOf(0, 2), awaitItem())
          awaitComplete()
        }
    }
  }
}
