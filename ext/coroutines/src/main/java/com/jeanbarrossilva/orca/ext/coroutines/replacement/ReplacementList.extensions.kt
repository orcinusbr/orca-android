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

package com.jeanbarrossilva.orca.ext.coroutines.replacement

/** Creates an empty [ReplacementList]. */
internal fun <T> emptyReplacementList(): ReplacementList<T, T> {
  return ReplacementList(mutableListOf()) { it }
}

/**
 * Creates an empty [ReplacementList].
 *
 * @param selector Returns the value by which elements should be compared when replacing them.
 */
internal fun <I, O> emptyReplacementList(selector: (I) -> O): ReplacementList<I, O> {
  return ReplacementList(mutableListOf(), selector)
}

/**
 * Creates a [ReplacementList] with the given [elements].
 *
 * @param elements Elements to be added to the [ReplacementList].
 */
internal fun <T> replacementListOf(vararg elements: T): ReplacementList<T, T> {
  return ReplacementList(mutableListOf(*elements)) { it }
}

/**
 * Creates a [ReplacementList] with the given [elements].
 *
 * @param elements Elements to be added to the [ReplacementList].
 * @param selector Returns the value by which elements should be compared when replacing them.
 */
internal fun <I, O> replacementListOf(
  vararg elements: I,
  selector: (I) -> O
): ReplacementList<I, O> {
  return ReplacementList(mutableListOf(*elements), selector)
}
