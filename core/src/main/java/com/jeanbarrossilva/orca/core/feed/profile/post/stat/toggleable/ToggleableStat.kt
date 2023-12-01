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
