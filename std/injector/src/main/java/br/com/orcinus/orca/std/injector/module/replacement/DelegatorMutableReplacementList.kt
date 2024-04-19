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

import java.util.Objects

/**
 * Selector that returns the element that has been provided to it, denoting that comparisons in a
 * [MutableReplacementList] should be performed by comparing its structure to another one's.
 *
 * @see MutableReplacementList.selector
 */
private val structuralEqualityBasedSelector = { element: Any? -> element }

/**
 * Implementation of [MutableReplacementList] that delegates [MutableList]-like functionality to the
 * [delegate] and is returned by [mutableReplacementListOf].
 *
 * @param E Element to be contained.
 * @param S Object with which comparison for determining whether an element gets either added or
 *   replaced is performed.
 * @param delegate [MutableList] to which this [DelegatorMutableReplacementList]'s functionality
 *   will be delegated, except for that of [onAddition].
 */
private class DelegatorMutableReplacementList<E, S>(
  private val delegate: MutableList<E>,
  override val caching: Caching<E, S>,
  override val selector: (E) -> S
) : MutableReplacementList<E, S>(), MutableList<E> by delegate {
  override fun equals(other: Any?): Boolean {
    return other is DelegatorMutableReplacementList<*, *> &&
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

  override fun add(index: Int, element: E) {
    super.add(index, element)
  }

  override fun addAll(index: Int, elements: Collection<E>): Boolean {
    return super.addAll(index, elements)
  }

  override fun clear() {
    super.clear()
  }

  override fun onAddition(index: Int, element: E) {
    delegate.add(index, element)
  }

  override fun onClearance() {
    delegate.clear()
  }
}

/**
 * Creates a [MutableReplacementList] with the given [elements].
 *
 * Selection caching is disabled, given that it would be unnecessary and rather inefficient because
 * these are just the elements on which the selector was invoked themselves; that means that they'd
 * be stored not only once, but twice.
 *
 * When denoting whether one of the elements should be replaced when a new one is being added, the
 * replacement and the current candidate over which iteration is taking place will be compared
 * structurally, meaning that it will be replaced when the replacement is passed into its
 * [Any.equals] method and it returns `true`.
 *
 * @param E Element to be contained.
 * @param elements Elements to be added to the [MutableReplacementList].
 */
fun <E> mutableReplacementListOf(vararg elements: E): MutableReplacementList<E, E> {
  val delegate = mutableListOf(*elements)
  @Suppress("UNCHECKED_CAST") val selector = structuralEqualityBasedSelector as (E) -> E
  val caching = MutableReplacementList.Caching.Disabled(selector)
  return DelegatorMutableReplacementList(delegate, caching, selector)
}

/**
 * Creates a [MutableReplacementList] with the given [elements].
 *
 * Selection caching is enabled, meaning that each selector invocation result is associated to the
 * element on which it was performed, allowing for it to be later retrieved when, for example,
 * comparing an element in the [MutableReplacementList] to another one that may or may not be in it
 * via [contains].
 *
 * In case the specified [selector] merely returns the element over which iteration takes place
 * itself, the [mutableReplacementListOf] creator method *without* a selector parameter should be
 * called to create the [MutableReplacementList] instead, given that it disables caching that would
 * otherwise be unnecessary and inefficient and already overrides such selector.
 *
 * @param E Element to be contained.
 * @param S Object with which comparison for determining whether an element gets either added or
 *   replaced is performed.
 * @param elements Elements to be added to the [MutableReplacementList].
 * @param selector Provides the value by which each element should be compared when replaced.
 */
fun <E, S> mutableReplacementListOf(
  vararg elements: E,
  selector: (E) -> S
): MutableReplacementList<E, S> {
  val delegate = mutableListOf(*elements)
  val caching = MutableReplacementList.Caching.Enabled(selector)
  return DelegatorMutableReplacementList(delegate, caching, selector)
}
