package com.jeanbarrossilva.orca.feature.feed.test

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.test.runTest

/**
 * Waits until the result of [condition] is `true`.
 *
 * @param condition Returns the expectation to be met.
 **/
internal fun waitUntil(condition: () -> Boolean) {
    runTest {
        suspendCoroutine {
            @Suppress("ControlFlowWithEmptyBody")
            while (!condition()) {
            }

            it.resume(Unit)
        }
    }
}
