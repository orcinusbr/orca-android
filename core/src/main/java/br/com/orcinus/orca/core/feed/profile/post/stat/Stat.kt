/*
 * Copyright © 2023–2024 Orcinus
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

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.ext.coroutines.getValue
import br.com.orcinus.orca.ext.coroutines.setValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Statistic for counting and retrieving a specific set of objects that is the basic building block
 * for a statistical component of a [Post] — encompassing, but not limited to: its comments, its
 * favorites and its reposts. These provide both the quantity of replies or [Profile]s that have
 * interacted with it and the elements that represent these interactions (which are [T]).
 *
 * Also, note that the amounts returned by the [count] don't necessarily reflect the actual size of
 * the [List] lastly emitted to the result of [get]. Although all implementations of this class
 * should be as precise as possible when calculating what the total number of elements is, there is
 * no precise _and_ efficient way of guaranteeing parity (that is, throwing an [Exception] in case
 * the reported amount and the actual one mismatch).
 *
 * @param T Element which can be retrieved.
 * @param count Initial amount of elements.
 * @see Post.comment
 * @see Post.favorite
 * @see Post.repost
 * @see List.size
 */
abstract class Stat<T> internal constructor(count: Int) {
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
  abstract fun get(page: Int): Flow<List<T>>
}
