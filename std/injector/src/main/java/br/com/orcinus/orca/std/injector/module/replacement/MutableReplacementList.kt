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
 */
abstract class MutableReplacementList<E, S> internal constructor() : MutableList<E> {
  /** Defines the caching behavior for invocations of the [selector] on same elements. */
  internal abstract val caching: Caching<E, S>

  /** Provides the value by which each element should be compared when replaced. */
  internal abstract val selector: (E) -> S

  /** Denotes that an instance of an object hasn't yet been obtained. */
  private object None

  /**
   * Strategy for dealing with selections of same elements.
   *
   * @param E Element whose selection will be performed.
   * @param S Result of selecting an element.
   */
  sealed class Caching<E, S> {
    /**
     * Denotes that selections shouldn't be cached.
     *
     * @param E Element whose selection will be performed.
     * @param S Result of selecting an element.
     * @param selector Selects an element.
     */
    class Disabled<E, S> internal constructor(private val selector: (E) -> S) : Caching<E, S>() {
      override fun on(element: E): S {
        return selector(element)
      }

      override fun remove(element: E) {}

      override fun clear() {}
    }

    /**
     * Denotes that selections should be cached, preventing recalculations for elements that are
     * equal.
     *
     * @param E Element whose selection will be performed.
     * @param S Result of selecting an element.
     * @param selector Selects an element.
     */
    class Enabled<E, S> internal constructor(private val selector: (E) -> S) : Caching<E, S>() {
      /**
       * Selections (meaning the result of invoking the [selector] on an element) that have already
       * been performed.
       */
      private val selections = hashMapOf<E, S>()

      override fun on(element: E): S {
        return selections.getOrPut(element) { selector(element) }
      }

      override fun remove(element: E) {
        selections.remove(element)
      }

      override fun clear() {
        selections.clear()
      }
    }

    /**
     * Applies this strategy to the given [element].
     *
     * @param element Element whose selection may or may not be cached.
     */
    internal abstract fun on(element: E): S

    /**
     * Removes the selection that has been performed on the [element].
     *
     * @param element Element on which the [selector] may have been invoked and whose selection will
     *   be removed.
     */
    internal abstract fun remove(element: E)

    /** Clears any selection that has been cached. */
    internal abstract fun clear()
  }

  /**
   * Returns whether the [element] is present in this [MutableReplacementList], comparing it to the
   * currently existing ones by their [selector]s.
   *
   * @param element Element whose presence will be checked.
   */
  override fun contains(element: E): Boolean {
    var selection: Any? = None
    return any {
      if (selection === None) {
        selection = selector(element)
      }
      selection == caching.on(it)
    }
  }

  /**
   * Returns whether all of the [elements] are present in this [MutableReplacementList], comparing
   * them to the currently existing ones by their [selector]s.
   *
   * @param elements Elements whose presence will be checked.
   */
  override fun containsAll(elements: Collection<E>): Boolean {
    return elements.all(::contains)
  }

  /**
   * Either adds the given [element] to the end of this [MutableReplacementList] or replaces the
   * existing one that matches it based on its [selector], placing it at the same index at which the
   * previous one was.
   *
   * @param element Element to be added or by which the existing one that matches it will be
   *   replaced.
   */
  override fun add(element: E): Boolean {
    val index = lastIndex.inc()
    add(index, element)
    return true
  }

  /**
   * Either adds all of the given [elements] to the end of this [MutableReplacementList] or replaces
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
   * Either adds all of the given [elements] or replaces the existing ones that matches them based
   * on their [selector]s, placing them at the same index at which the previous one was.
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

  /**
   * Removes both all of the elements in this [MutableReplacementList] and the results of [selector]
   * invocations on elements that have been iterated through, denoting that these selections should
   * be computed again the next time a comparison operation is performed on the elements themselves.
   */
  override fun clear() {
    caching.clear()
    onClearance()
  }

  /**
   * Callback that gets called when the [element] is requested to be added.
   *
   * @param index Index at which the element *should* be added.
   * @param element Element to be appended.
   */
  protected abstract fun onAddition(index: Int, element: E)

  /**
   * Callback that gets called when this [MutableReplacementList] is requested to be cleared, after
   * all of the selections have been cleared in case they've been previously cached (the latter
   * depending on the defined caching strategy).
   *
   * @see caching
   */
  protected abstract fun onClearance()

  /**
   * Places all of the [elements].
   *
   * @param elements Elements to be placed.
   * @return Result of having placed the [elements] or [None] when they haven't been placed at all.
   */
  private fun place(elements: Collection<E>): Any? {
    val firstElement = elements.firstOrNull() ?: return None
    val firstElementSelection = caching.on(firstElement)
    var outerIndex = size.inc() - indexOfFirst { firstElementSelection != caching.on(it) }
    var lastPlacement: Any? = None
    for (innerIndex in elements.indices) {
      val element = if (innerIndex == 0) firstElement else elements.elementAt(innerIndex)
      lastPlacement = place(outerIndex++, element)
    }
    return lastPlacement
  }

  /**
   * Places the [element].
   *
   * @param index Index at which the element *may* be added.
   * @param element Element to be place.
   */
  private fun place(index: Int, element: E) {
    var selection: Any? = None
    val replacementIndex = indexOfFirst {
      if (selection === None) {
        selection = selector(element)
      }
      selection == caching.on(it)
    }
    val isNonexistent = replacementIndex == -1
    if (isNonexistent) {
      val additionIndex = maxOf(0, index)
      onAddition(additionIndex, element)
    } else {
      @Suppress("UNCHECKED_CAST") replace(replacementIndex, element, selection as S)
    }
  }

  /**
   * Replaces the element whose [selector] equals that of the [element].
   *
   * @param index Index at which the element to be replaced is.
   * @param element Element by which the matching one will be replaced.
   * @param selection Result of invoking the [selector] on the [element].
   */
  private fun replace(index: Int, element: E, selection: S) {
    for (candidateIndex in indices) {
      val candidate = elementAt(candidateIndex)
      val isReplaceable = caching.on(candidate) == selection
      if (isReplaceable) {
        caching.remove(candidate)
        removeAt(index)
        onAddition(index, element)
      }
    }
  }
}
