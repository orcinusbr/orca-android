/*
 * Copyright © 2023-2024 Orca
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

package br.com.orcinus.orca.core.feed.profile.post.stat

import br.com.orcinus.orca.ext.coroutines.getValue
import br.com.orcinus.orca.ext.coroutines.setValue
import br.com.orcinus.orca.std.buildable.Buildable
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
  private val countMutableFlow = MutableStateFlow(count)

  /** [StateFlow] to which the amount of elements will be emitted. */
  val countFlow = countMutableFlow.asStateFlow()

  /** Current amount of elements. */
  var count by countMutableFlow
    internal set

  /**
   * Gets the [Flow] to which the elements related to this [Stat] will be emitted.
   *
   * @param page Page at which the elements to be emitted are.
   */
  open fun get(page: Int): Flow<List<T>> {
    return flowOf(emptyList())
  }
}
