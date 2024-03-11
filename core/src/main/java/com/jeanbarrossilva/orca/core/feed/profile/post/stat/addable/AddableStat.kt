/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.core.feed.profile.post.stat.addable

import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.std.buildable.Buildable

/**
 * [Stat] to and from which elements can be added and removed.
 *
 * @param T Element to be either added or removed.
 * @param count Initial amount of elements.
 */
@Buildable
abstract class AddableStat<T> internal constructor(count: Int = 0) : Stat<T>(count) {
  /**
   * Adds the given [element].
   *
   * @param element Element to be added.
   */
  suspend fun add(element: T) {
    count++
    onAdd(element)
  }

  /**
   * Removes the given [element].
   *
   * @param element Element to be removed.
   */
  suspend fun remove(element: T) {
    count--
    onRemove(element)
  }

  /**
   * Callback called when the given [element] is requested to be added.
   *
   * @param element Element to be added.
   */
  protected open suspend fun onAdd(element: T) {}

  /**
   * Callback called when the given [element] is requested to be removed.
   *
   * @param element Element to be removed.
   */
  protected open suspend fun onRemove(element: T) {}
}
