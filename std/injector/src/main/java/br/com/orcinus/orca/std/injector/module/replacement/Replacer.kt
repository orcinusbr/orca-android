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

package br.com.orcinus.orca.std.injector.module.replacement

/**
 * [Collection] in which elements can be placed based on their presence by being either appended or
 * replaced.
 *
 * @param E Element to be contained.
 * @param S Object with which comparison for determining whether an element gets either added or
 *   replaced is performed.
 * @param P Result of either appending or removing an element.
 * @see place
 */
abstract class Replacer<E, S, P> : Collection<E> {
  /** Defines the caching behavior for invocations of the [selector] on same elements. */
  internal abstract val caching: Caching<E, S>

  /** Provides the value by which each element should be compared when replaced. */
  internal abstract val selector: (E) -> S

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

  /** Denotes that an instance of an object hasn't yet been obtained. */
  object None

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
   * Returns whether all the [elements] are present in this [MutableReplacementList], comparing them
   * to the currently existing ones by their [selector]s.
   *
   * @param elements Elements whose presence will be checked.
   */
  override fun containsAll(elements: Collection<E>): Boolean {
    return elements.all(::contains)
  }

  /**
   * Places all of the [elements].
   *
   * @param elements Elements to be placed.
   * @return Result of having placed the [elements] or [None] when they haven't been placed at all.
   */
  protected fun place(elements: Collection<E>): Any? {
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
   * @param index Index at which the element may be added.
   * @param element Element to be place.
   * @return Result of having placed the [element] at the [index] or [None] when it hasn't been
   *   placed at all.
   */
  protected fun place(index: Int, element: E): Any? {
    var selection: Any? = None
    val replacementIndex = indexOfFirst {
      if (selection === None) {
        selection = selector(element)
      }
      selection == caching.on(it)
    }

    return if (replacementIndex == -1) {
      val additionIndex = maxOf(0, index)
      append(None, additionIndex, element)
    } else {
      @Suppress("UNCHECKED_CAST") replace(replacementIndex, element, selection as S)
    }
  }

  /**
   * Appends the [element].
   *
   * @param placement Result of the previous element that has been either appended or prepared for
   *   replacement. Can be either [P] (in the previously described scenario) or [None] when there
   *   hasn't been a placement prior to this one.
   * @param index Index at which the element *should* be added.
   * @param element Element to be appended.
   * @return Result of having appended the [element] at the [index].
   */
  protected abstract fun append(placement: Any?, index: Int, element: E): P

  /**
   * Callback that gets called when the element at the [index] is requested to be prepared for an
   * upcoming replacement.
   *
   * @param index Index at which the element to be replaced is.
   * @return Result of having prepared the element at the [index] to have another one being added to
   *   where it currently is.
   */
  protected abstract fun onPreparationForReplacement(index: Int): P

  /**
   * Removes the results of [selector] invocations on elements that have been iterated through,
   * denoting that these selections should be computed again the next time a comparison operation is
   * performed on the elements themselves.
   */
  protected fun unselect() {
    caching.clear()
  }

  /**
   * Replaces the element whose [selector] equals that of the [element].
   *
   * @param index Index at which the element to be replaced is.
   * @param element Element by which the matching one will be replaced.
   * @param selection Result of invoking the [selector] on the [element].
   * @return Result of having replaced the element at the [index] by the given one or [None] when no
   *   matching one is found.
   */
  private fun replace(index: Int, element: E, selection: S): Any? {
    for (candidateIndex in indices) {
      val candidate = elementAt(candidateIndex)
      val isReplaceable = selector(candidate) == selection
      if (isReplaceable) {
        return append(prepareForReplacement(index, candidate), index, element)
      }
    }
    return None
  }

  /**
   * Prepares the [element] for an upcoming replacement.
   *
   * @param index Index at which the [element] is.
   * @param element Element to soon be replaced.
   * @return Result of having prepared the element at the [index] to have another one being added to
   *   where it currently is.
   */
  private fun prepareForReplacement(index: Int, element: E): Any? {
    caching.remove(element)
    return onPreparationForReplacement(index)
  }

  companion object {
    /**
     * Selector that returns the element that has been provided to it, denoting that comparisons in
     * a [Replacer] should be performed by comparing its structure to another one's (behaving just
     * like [Any.equals]).
     *
     * @see selector
     */
    private val structuralEqualityBasedSelector = { element: Any? -> element }

    /**
     * Provides the appropriate caching strategy for creating a [Replacer] whose selector is a
     * custom one.
     *
     * For this kind of [Replacer], selection caching is enabled, meaning that each selector
     * invocation result is associated to the element on which it was performed, allowing for it to
     * be later retrieved when, for example, comparing an element in the [Replacer] to another one
     * that may or may not be in it via [contains].
     *
     * In case the specified [selector] merely returns the element over which iteration takes place
     * itself, [withStructuralEqualityBasedSelector] should be used to create the [Replacer]
     * instead, given that it disables caching that would otherwise be unnecessary and inefficient
     * and already provides such selector.
     *
     * @param selector Provides the value by which each element should be compared when replaced.
     * @param creation Creates the [Replacer] with the given [Caching].
     * @see Replacer.selector
     */
    fun <E, S, R : Replacer<E, S, *>> withCustomSelector(
      selector: (E) -> S,
      creation: (Caching<E, S>) -> R
    ): R {
      val caching = Caching.Enabled(selector)
      return creation(caching)
    }

    /**
     * Provides a selector for comparing the elements' by their own structure, alongside the
     * appropriate caching strategy for creating a [Replacer].
     *
     * For a [Replacer] of such a kind, caching selections would be unnecessary and rather
     * inefficient because these are just the elements on which the selector was invoked themselves;
     * that means that they'd be stored not only once, but twice.
     *
     * @param creation Creates the [Replacer] with the given [Caching] and structural-equality-based
     *   selector.
     * @see Replacer.selector
     */
    fun <E, R : Replacer<E, E, *>> withStructuralEqualityBasedSelector(
      creation: (Caching<E, E>, selector: (E) -> E) -> R
    ): R {
      @Suppress("UNCHECKED_CAST") val selector = structuralEqualityBasedSelector as (E) -> E
      val caching = Caching.Disabled(selector)
      return creation(caching, selector)
    }
  }
}
