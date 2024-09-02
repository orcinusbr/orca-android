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

package br.com.orcinus.orca.core.sample.feed.profile.post.stat.addable

import br.com.orcinus.orca.core.feed.profile.post.stat.addable.AddableStat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** [AddableStat] whose elements are read from and written into memory. */
internal class SampleAddableStat<T> : AddableStat<T>(count = 0) {
  /** [MutableStateFlow] containing the elements. */
  private val elementsFlow = MutableStateFlow(emptyList<T>())

  override fun get(page: Int): StateFlow<List<T>> {
    return elementsFlow
  }

  override suspend fun onAddition(element: T) {
    elementsFlow.value += element
  }

  override suspend fun onRemoval(element: T) {
    elementsFlow.value -= element
  }
}
