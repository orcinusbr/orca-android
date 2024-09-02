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

package br.com.orcinus.orca.core.feed.profile.post.stat.addable

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.feed.profile.post.stat.Stat

/**
 * [Stat] to and from which elements can be added and removed.
 *
 * @param T Element to be either added or removed.
 * @param count Initial amount of elements.
 */
abstract class AddableStat<T> @InternalCoreApi constructor(count: Int) : Stat<T>(count) {
  /**
   * Adds the given [element].
   *
   * @param element Element to be added.
   */
  suspend fun add(element: T) {
    count++
    onAddition(element)
  }

  /**
   * Removes the given [element].
   *
   * @param element Element to be removed.
   */
  suspend fun remove(element: T) {
    count--
    onRemoval(element)
  }

  /**
   * Callback called when the given [element] is requested to be added.
   *
   * @param element Element to be added.
   */
  protected abstract suspend fun onAddition(element: T)

  /**
   * Callback called when the given [element] is requested to be removed.
   *
   * @param element Element to be removed.
   */
  protected abstract suspend fun onRemoval(element: T)
}
