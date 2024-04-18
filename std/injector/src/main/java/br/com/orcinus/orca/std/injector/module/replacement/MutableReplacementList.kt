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

package br.com.orcinus.orca.std.injector.module.replacement

/**
 * [MutableList] that replaces a given element when its [selector] matches that of the other one
 * being added, maintaining its index.
 *
 * @param E Element to be contained.
 * @param S Object with which comparison for determining whether an element gets either added or
 *   replaced is performed.
 * @see append
 * @see replace
 */
abstract class MutableReplacementList<E, S> internal constructor() :
  Replacer<E, S, Unit>(), MutableList<E> {
  /**
   * Either adds the given [element] to the end of this [MutableReplacementList] or replaces the
   * existing one that matches it based on its [selector], placing it at the same index at which the
   * previous one was.
   *
   * @param element Element to be added or by which the existing one that matches it will be
   *   replaced.
   */
  override fun add(element: E): Boolean {
    add(lastIndex, element)
    return true
  }

  /**
   * Either adds all the given [elements] to the end of this [MutableReplacementList] or replaces
   * the existing ones that matches them based on their [selector]s, placing them at the same index
   * at which the previous one was.
   *
   * @param elements Elements to be added or by which the existing ones that match them will be
   *   replaced.
   */
  override fun addAll(elements: Collection<E>): Boolean {
    return addAll(lastIndex, elements)
  }

  /**
   * Either adds all the given [elements] or replaces the existing ones that matches them based on
   * their [selector]s, placing them at the same index at which the previous one was.
   *
   * @param index Index at which the [elements] should be added when no other ones that match them
   *   are found.
   * @param elements Elements to be added or by which the existing ones that match them will be
   *   replaced.
   */
  override fun addAll(index: Int, elements: Collection<E>): Boolean {
    place(elements)
    return true
  }

  /**
   * Either adds the given [element] or replaces the existing one that matches it based on its
   * [selector], placing it at the same index at which the previous one was.
   *
   * @param index Index at which the [element] should be added when no other one that matches it is
   *   found.
   * @param element Element to be added or by which the existing one that matches it will be
   *   replaced.
   */
  override fun add(index: Int, element: E) {
    place(index, element)
  }

  override fun clear() {
    unselect()
    onClearance()
  }

  /**
   * Callback that gets called when this [MutableReplacementList] is requested to be cleared, after
   * it has been properly unselected.
   *
   * @see unselect
   */
  protected abstract fun onClearance()
}
