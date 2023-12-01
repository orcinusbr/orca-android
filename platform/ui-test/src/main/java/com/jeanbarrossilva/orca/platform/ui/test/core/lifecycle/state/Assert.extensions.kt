/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
