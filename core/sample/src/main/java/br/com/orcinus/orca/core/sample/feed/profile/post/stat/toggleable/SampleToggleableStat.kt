/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.sample.feed.profile.post.stat.toggleable

import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * [ToggleableStat] whose elements are stored and toggled in memory.
 *
 * @param T Element which can be added or retrieved.
 * @property toggle Object that will get added this is enabled or removed when it is disabled.
 */
internal class SampleToggleableStat<T>(private val toggle: T) : ToggleableStat<T>(count = 0) {
  /** [MutableStateFlow] containing the elements. */
  private val elementsFlow = MutableStateFlow(emptyList<T>())

  override suspend fun get(page: Int): StateFlow<List<T>> {
    return elementsFlow
  }

  override suspend fun onSetEnabled(isEnabled: Boolean) {
    if (isEnabled) {
      elementsFlow.value -= toggle
    } else {
      elementsFlow.value += toggle
    }
  }
}
