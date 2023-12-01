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

package com.jeanbarrossilva.orca.core.feed.profile.post.stat

import com.jeanbarrossilva.orca.std.buildable.Buildable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Specific statistic whose amounts emitted to the [count] doesn't necessarily reflect the summed
 * [size][List.size] of all [List]s emitted to the result of [get]. Although all core variants
 * should be as precise as possible when defining what the total amount of elements counted by this
 * [Stat] is, there is no precise and efficient way of guaranteeing parity.
 *
 * An instance of this class can be created via its factory methods, through which it can be
 * properly configured.
 *
 * @param count Initial amount of elements.
 * @see get
 */
@Buildable
abstract class Stat<T> internal constructor(count: Int = 0) {
  /**
   * [MutableStateFlow] that keeps track of the total amount of elements comprehended by this
   * [Stat].
   */
  internal val countMutableFlow = MutableStateFlow(count)

  /** [StateFlow] to which the amount of elements will be emitted. */
  val countFlow = countMutableFlow.asStateFlow()

  /** Current amount of elements. */
  val count
    get() = countFlow.value

  /**
   * Gets the [Flow] to which the elements related to this [Stat] will be emitted.
   *
   * @param page Page at which the elements to be emitted are.
   */
  open fun get(page: Int): Flow<List<T>> {
    return flowOf(emptyList())
  }
}
