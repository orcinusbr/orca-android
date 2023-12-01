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

package com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable

import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.std.buildable.Buildable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * [Stat] that can have its enable-ability toggled.
 *
 * @param count Initial amount of elements.
 */
@Buildable
abstract class ToggleableStat<T> internal constructor(count: Int = 0) : Stat<T>(count) {
  /** [MutableStateFlow] that gets emitted to whenever this [ToggleableStat] is toggled. */
  private val isEnabledMutableFlow = MutableStateFlow(false)

  /** [StateFlow] to which the current enable-ability state will be emitted. */
  val isEnabledFlow = isEnabledMutableFlow.asStateFlow()

  /** Whether this [ToggleableStat] is currently enabled. */
  val isEnabled
    get() = isEnabledFlow.value

  /** Toggles whether this [ToggleableStat] is enabled. */
  suspend fun toggle() {
    setEnabled(!isEnabled)
    isEnabledMutableFlow.value = !isEnabled
    countMutableFlow.value = if (isEnabled) countFlow.value.inc() else countFlow.value.dec()
  }

  /** Enables this [ToggleableStat]. */
  suspend fun enable() {
    if (!isEnabled) {
      setEnabled(true)
      isEnabledMutableFlow.value = true
      countMutableFlow.value++
    }
  }

  /** Disables this [ToggleableStat]. */
  suspend fun disable() {
    if (isEnabled) {
      setEnabled(false)
      isEnabledMutableFlow.value = false
      countMutableFlow.value--
    }
  }

  /**
   * Defines whether this [ToggleableStat] is enabled.
   *
   * @param isEnabled Whether it's being enabled or disabled.
   */
  protected open suspend fun setEnabled(isEnabled: Boolean) {}
}
