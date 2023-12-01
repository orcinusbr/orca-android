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

package com.jeanbarrossilva.orca.platform.ui.core.lifecycle.state

/**
 * Compares this [CompleteLifecycleState] with the given [other]; if it is `null`, then it is
 * considered to be less than [other].
 *
 * @param other [CompleteLifecycleState] to compare this one with.
 */
infix operator fun CompleteLifecycleState?.compareTo(other: CompleteLifecycleState): Int {
  return this?.compareTo(other) ?: -1
}

/**
 * Whether this [CompleteLifecycleState] equals to or is greater than the given [state].
 *
 * @param state [CompleteLifecycleState] to compare this one with.
 */
fun CompleteLifecycleState?.isAtLeast(state: CompleteLifecycleState): Boolean {
  return this != null && compareTo(state) >= 0
}

/**
 * Provides the [CompleteLifecycleState] that succeeds this one; if it's `null`, provides the
 * [created][CompleteLifecycleState.CREATED] [CompleteLifecycleState].
 */
internal fun CompleteLifecycleState?.next(): CompleteLifecycleState? {
  return if (this == null) CompleteLifecycleState.CREATED else next()
}
