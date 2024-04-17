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
 * @see add
 * @see replace
 */
abstract class ReplacementList<E, S> internal constructor() : MutableList<E> {
  /** Provides the value by which each element should be compared when replaced. */
  protected abstract val selector: (E) -> S

  /** Denotes that an instance of an object is yet to be obtained. */
  internal object None

  /**
   * Returns whether the [element] is present in this [ReplacementList], comparing it to the
   * currently existing ones by their [selector]s.
   *
   * @param element Element whose presence will be checked.
   */
  override fun contains(element: E): Boolean {
    return any { selector(element) == selector(it) }
  }

  /**
   * Returns whether all the [elements] are present in this [ReplacementList], comparing them to the
   * currently existing ones by their [selector]s.
   *
   * @param elements Elements whose presence will be checked.
   */
  override fun containsAll(elements: Collection<E>): Boolean {
    return elements.all(::contains)
  }

  /**
   * Either adds the given [element] to the end of this [ReplacementList] or replaces the existing
   * one that matches it based on its [selector], placing it at the same index at which the previous
   * one was.
   *
   * @param element Element to be added or by which the existing one that matches it will be
   *   replaced.
   */
  override fun add(element: E): Boolean {
    add(lastIndex, element)
    return true
  }

  /**
   * Either adds all the given [elements] to the end of this [ReplacementList] or replaces the
   * existing ones that matches them based on their [selector]s, placing them at the same index at
   * which the previous one was.
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
    val firstElement = elements.firstOrNull() ?: return false
    val firstElementSelection = selector(firstElement)
    var outerIndex = size - indexOfFirst { firstElementSelection != selector(it) }
    for (innerIndex in elements.indices) {
      val element = if (innerIndex == 0) firstElement else elements.elementAt(innerIndex)
      add(outerIndex++, element)
    }
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
    var selection: Any? = None
    val replacementIndex = indexOfFirst {
      if (selection === None) {
        selection = selector(element)
      }
      selection == selector(it)
    }
    if (replacementIndex == -1) {
      val additionIndex = maxOf(0, index)
      onAdd(additionIndex, element)
    } else {
      @Suppress("UNCHECKED_CAST") replace(replacementIndex, element, selection as S)
    }
  }

  /**
   * Replaces the element whose [selector] equals that of the [replacement].
   *
   * @param index Index at which the element to be replaced is.
   * @param replacement Element by which the matching one will be replaced.
   * @param selection Result of invoking the [selector] on the [replacement].
   */
  protected abstract fun replace(index: Int, replacement: E, selection: S)

  /**
   * Callback that gets called when an element that was not previously in this [ReplacementList] is
   * requested to be added to it.
   *
   * @param index Index at which the [element] should be added.
   * @param element Element to be added, for which a matching existing one hasn't been found.
   * @return Whether the [element] has been successfully added.
   */
  protected abstract fun onAdd(index: Int, element: E)
}
