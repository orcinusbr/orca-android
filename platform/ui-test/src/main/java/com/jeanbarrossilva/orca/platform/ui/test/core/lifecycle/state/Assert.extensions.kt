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
 */
fun assertIsAtLeast(expected: CompleteLifecycleState, actual: CompleteLifecycleState?) {
  assertTrue("$actual is < $expected.", actual.isAtLeast(expected))
}
