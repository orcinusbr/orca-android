/*
 * Copyright © 2023-2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.feed.profile.post.stat.toggleable

import br.com.orcinus.orca.core.feed.profile.post.stat.Stat
import br.com.orcinus.orca.ext.coroutines.getValue
import br.com.orcinus.orca.ext.coroutines.setValue
import br.com.orcinus.orca.std.buildable.Buildable
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
  var isEnabled by isEnabledMutableFlow
    private set

  /** Toggles whether this [ToggleableStat] is enabled. */
  suspend fun toggle() {
    setEnabled(!isEnabled)
  }

  /** Enables this [ToggleableStat]. */
  suspend fun enable() {
    if (!isEnabled) {
      setEnabled(true)
    }
  }

  /** Disables this [ToggleableStat]. */
  suspend fun disable() {
    if (isEnabled) {
      setEnabled(false)
    }
  }

  /**
   * Callback run whenever the enableability of this [ToggleableStat] is toggled.
   *
   * @param isEnabled Whether it's being enabled.
   */
  protected open suspend fun onSetEnabled(isEnabled: Boolean) {}

  /**
   * Defines whether this [ToggleableStat] is enabled.
   *
   * @param isEnabled Whether it should be enabled.
   */
  private suspend fun setEnabled(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    count += if (isEnabled) 1 else -1
    onSetEnabled(isEnabled)
  }
}
