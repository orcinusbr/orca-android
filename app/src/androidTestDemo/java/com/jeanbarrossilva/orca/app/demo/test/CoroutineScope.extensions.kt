package com.jeanbarrossilva.orca.app.demo.test

import kotlinx.coroutines.test.runTest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Waits indefinitely until the [condition] results into true.
 *
 * @param condition Returns whether the code execution should stop being suspended.
 */
internal fun waitUntil(condition: () -> Boolean) {
  runTest {
    suspendCoroutine {
      @Suppress("ControlFlowWithEmptyBody") while (!condition()) {}

      it.resume(Unit)
    }
  }
}
