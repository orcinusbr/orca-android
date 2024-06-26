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

package br.com.orcinus.orca.ext.coroutines.replacement

import java.util.Objects

/**
 * Selector that simply returns the element provided to it, denoting that comparisons in a
 * [ReplacementList] should be performed by comparing its structure to that of another one,
 * consequently denoting that the replacement will be passed into the [Any.equals] method of the
 * element on which iteration takes place and it will be replaced if it returns `true`.
 *
 * @see ReplacementList.selector
 */
private val structuralEqualityBasedSelector = { element: Any? -> element }

/**
 * Implementation of [ReplacementList] that delegates [MutableList]-like functionality to the
 * pre-populated [delegate] and is returned by [replacementListOf]. Conflicting overrides should all
 * fall back to [ReplacementList]'s behavior.
 *
 * @param E Element to be contained.
 * @param S Object with which comparison for determining whether an element gets either added or
 *   replaced is performed.
 * @param delegate [MutableList] to which [MutableList] functionality will be delegated.
 */
private class DelegatorReplacementList<E, S>(
  private val delegate: MutableList<E>,
  override val caching: Caching<E, S>,
  override val selector: (E) -> S
) : ReplacementList<E, S>(), MutableList<E> by delegate {
  override fun equals(other: Any?): Boolean {
    return other is DelegatorReplacementList<*, *> &&
      delegate == other.delegate &&
      caching == other.caching &&
      selector == other.selector
  }

  override fun hashCode(): Int {
    return Objects.hash(delegate, caching, selector)
  }

  override fun toString(): String {
    return delegate.toString()
  }

  override fun contains(element: E): Boolean {
    return super.contains(element)
  }

  override fun containsAll(elements: Collection<E>): Boolean {
    return super.containsAll(elements)
  }

  override fun add(element: E): Boolean {
    return super.add(element)
  }

  override fun addAll(elements: Collection<E>): Boolean {
    return super.addAll(elements)
  }

  override fun addAll(index: Int, elements: Collection<E>): Boolean {
    return super.addAll(index, elements)
  }

  override fun add(index: Int, element: E) {
    super.add(index, element)
  }

  override fun removeAll(elements: Collection<E>): Boolean {
    return super.removeAll(elements)
  }

  override fun remove(element: E): Boolean {
    return super.remove(element)
  }

  override fun clear() {
    super.clear()
    delegate.clear()
  }

  override fun plus(element: E): ReplacementList<E, S> {
    return DelegatorReplacementList(delegate, caching, selector).apply { add(element) }
  }

  override fun minus(element: E): ReplacementList<E, S> {
    return DelegatorReplacementList(delegate, caching, selector).apply { remove(element) }
  }

  /**
   * Creates a [ReplacementList] from which the element whose selection equals to the given one is
   * removed.
   *
   * @param selection Result of invoking the [selector] on the element to be removed.
   */
  @JvmName("minusSelection")
  fun minus(selection: S): ReplacementList<E, S> {
    return DelegatorReplacementList(delegate, caching, selector).apply {
      for (index in indices) {
        val candidate = getOrNull(index) ?: return this
        val candidateSelection = caching.on(candidate)
        if (candidateSelection == selection) {
          removeAt(index)
        }
      }
    }
  }

  override fun append(index: Int, element: E) {
    delegate.add(index, element)
  }
}

/**
 * [MutableList] that replaces a given element when its [selector] matches that of the other one
 * being added, maintaining its index. Can be instantiated by calling one of the [replacementListOf]
 * creator methods.
 *
 * @param E Element to be contained.
 * @param S Object with which comparison for determining whether an element gets either appended or
 *   replaced is performed.
 */
abstract class ReplacementList<E, S> internal constructor() : MutableList<E> {
  /** Defines the caching behavior for invocations of the [selector] on same elements. */
  protected abstract val caching: Caching<E, S>

  /** Provides the value by which each element should be compared when replaced. */
  protected abstract val selector: (E) -> S

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
    internal class Disabled<E, S>(private val selector: (E) -> S) : Caching<E, S>() {
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
    internal class Enabled<E, S>(private val selector: (E) -> S) : Caching<E, S>() {
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
   * Returns whether the [element] is present in this [ReplacementList], comparing it to the
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
   * Returns whether all of the [elements] are present in this [ReplacementList], comparing them to
   * the currently existing ones by their [selector]s.
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
    val index = lastIndex.inc()
    add(index, element)
    return true
  }

  /**
   * Either adds all of the given [elements] to the end of this [ReplacementList] or replaces the
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
   * Removes all of the [elements] by comparing the result of having invoked the [selector] on them
   * to those of the existing ones in this [ReplacementList].
   *
   * @param elements Elements to be removed.
   * @return Whether all of the [elements] have been removed.
   */
  override fun removeAll(elements: Collection<E>): Boolean {
    var haveBeenRemoved = elements.isNotEmpty()
    for (innerIndex in elements.indices) {
      val element = elements.elementAt(innerIndex)
      val hasNotBeenRemoved = !remove(element)
      if (hasNotBeenRemoved) {
        haveBeenRemoved = false
      }
    }
    return haveBeenRemoved
  }

  /**
   * Removes the [element] by comparing the result of having invoked the [selector] on it to those
   * of the existing ones in this [ReplacementList].
   *
   * @param element Element to be removed.
   * @return Whether the [element] has been removed.
   */
  override fun remove(element: E): Boolean {
    var elementSelection: Any? = None
    var hasBeenRemoved = false
    for (index in indices) {
      val candidate =
        getOrElse(index) {
          return hasBeenRemoved
        }
      val selection = caching.on(candidate)
      if (elementSelection === None) {
        elementSelection = selector(element)
      }
      if (elementSelection == selection) {
        caching.remove(element)
        removeAt(index)
        hasBeenRemoved = true
      }
    }
    return hasBeenRemoved
  }

  /**
   * Removes both all of the elements in this [ReplacementList] and, depending on whether caching is
   * enabled, the results of [selector] invocations on elements on which iteration has been
   * performed, denoting that these selections should be computed again the next time the elements
   * are requested to be compared.
   */
  override fun clear() {
    caching.clear()
  }

  /**
   * Returns whether one of the elements provide the given [selection] when the [selector] is
   * invoked on them.
   *
   * @param selection Result of selecting an element whose presence will be checked.
   */
  @JvmName("containsSelection")
  operator fun contains(selection: S): Boolean {
    return any { caching.on(it) == selection }
  }

  /**
   * Obtains the element whose selection equals to the given one or `null` when no element's equals
   * to it.
   *
   * @param selection Result of invoking the [selector] on the element to be obtained.
   */
  fun getOrNull(selection: S): E? {
    return find { caching.on(it) == selection }
  }

  /**
   * Creates a [ReplacementList] to which the [element] is added, either by replacing an existing
   * one depending on the equality of their selections or appending it.
   *
   * @param element Element to be added.
   */
  abstract operator fun plus(element: E): ReplacementList<E, S>

  /**
   * Creates a [ReplacementList] from which the [element] whose selector matches that of an existing
   * one is removed.
   *
   * @param element Element to be removed.
   */
  abstract operator fun minus(element: E): ReplacementList<E, S>

  /**
   * Callback that gets called when the [element] is requested to be appended.
   *
   * @param index Index at which the element *should* be appended.
   * @param element Element to be appended.
   */
  protected abstract fun append(index: Int, element: E)

  /**
   * Places all of the [elements].
   *
   * @param elements Elements to be placed.
   * @return Result of having placed the [elements] or [None] when they haven't been placed at all.
   */
  private fun place(elements: Collection<E>) {
    val firstElement = elements.firstOrNull() ?: return
    val firstElementSelection = caching.on(firstElement)
    var outerIndex = size.inc() - indexOfFirst { firstElementSelection != caching.on(it) }
    for (innerIndex in elements.indices) {
      val element = if (innerIndex == 0) firstElement else elements.elementAt(innerIndex)
      place(outerIndex++, element)
    }
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
      append(additionIndex, element)
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
      val candidate = get(candidateIndex)
      val isReplaceable = caching.on(candidate) == selection
      if (isReplaceable) {
        caching.remove(candidate)
        removeAt(index)
        append(index, element)
      }
    }
  }
}

/**
 * Creates a [ReplacementList].
 *
 * Posteriorly adding elements to it can produce one of two possible outcomes: either the element
 * will replace a previously existing one when they're structurally equal, being put at the same
 * index at which the matching one was, or it will get appended.
 *
 * In case the elements need to be compared by anything other than their own structures, the
 * [replacementListOf] creator method that requires a selector to be passed in should be called
 * instead, as it provides this lambda that allows for returning the value based on which
 * element-to-element comparison should be performed.
 *
 * @param E Element to be contained.
 * @param elements Elements to be added to the [ReplacementList].
 * @see ReplacementList.add
 */
fun <E> replacementListOf(vararg elements: E): ReplacementList<E, E> {
  val delegate = mutableListOf(*elements)
  @Suppress("UNCHECKED_CAST") val selector = structuralEqualityBasedSelector as (E) -> E
  val caching = ReplacementList.Caching.Disabled(selector)
  return DelegatorReplacementList(delegate, caching, selector)
}

/**
 * Creates a [ReplacementList].
 *
 * Posteriorly adding elements to it can produce one of two possible outcomes: either the element
 * will replace a previously existing one when their selections (which is the result of invoking the
 * [selector] on them) are equal, being put at the same index at which the matching one was, or it
 * will get appended.
 *
 * When removing an element, what determines whether it matches an already-present one isn't its
 * structure, but its selection compared to those of pre-contained ones. For example: given a
 * `replacementListOf(0, 1, 2) { it % 2 == 0 }` (whose selector defines the identity of an element
 * as being its divisibility by 2, allowing only for a single even and a single odd [Int] to be
 * contained), `remove(4)` would remove both `0` and `2`.
 *
 * In case the specified [selector] merely returns the element on which iteration takes place
 * itself, the [replacementListOf] creator method *without* a selector parameter should be called
 * instead, given that it disables caching that would otherwise be unnecessary and inefficient and
 * already overrides such selector.
 *
 * @param E Element to be contained.
 * @param S Object with which comparison for determining whether an element gets either appended or
 *   replaced is performed.
 * @param elements Elements to be added to the [ReplacementList].
 * @param selector Provides the value by which each element should be compared when replaced.
 * @see ReplacementList.add
 * @see ReplacementList.remove
 */
fun <E, S> replacementListOf(vararg elements: E, selector: (E) -> S): ReplacementList<E, S> {
  val delegate = mutableListOf(*elements)
  val caching = ReplacementList.Caching.Enabled(selector)
  return DelegatorReplacementList(delegate, caching, selector)
}

/**
 * Creates a [ReplacementList] from which the element whose selection equals to the given one is
 * removed.
 *
 * @param E Element to be contained.
 * @param S Object with which comparison for determining whether an element gets either appended or
 *   replaced is performed.
 * @param selection Result of invoking the selector on the element to be removed.
 * @see ReplacementList.selector
 */
operator fun <E, S> ReplacementList<E, S>.minus(selection: S): ReplacementList<E, S> {
  return (this as DelegatorReplacementList<E, S>).minus(selection)
}
