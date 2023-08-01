package com.jeanbarrossilva.orca.platform.ui.test.core.lifecycle.state

import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.CompleteLifecycleState
import com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state.isAtLeast
import org.junit.Assert.assertTrue

/**
 * Asserts that [actual] is at least the [expected][expected] [CompleteLifecycleState].
 *
 * @param expected [CompleteLifecycleState] that [actual] should at least be.
 * @param actual [CompleteLifecycleState] to be compared to the [expected] one.
 * @see CompleteLifecycleState.isAtLeast
 **/
fun assertIsAtLeast(
    expected: CompleteLifecycleState,
    actual: CompleteLifecycleState?
) {
    assertTrue("$actual is < $expected.", actual.isAtLeast(expected))
}
