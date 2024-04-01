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

package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

/**
 * Returns a [List] containing exactly the same elements of this [Collection], except for the ones
 * that match the [predicate] and have been replaced by the result of [replacement].
 *
 * @param replacement Lambda that receives the candidate being currently iterated and returns the
 *   replacement of the one that matches the [predicate].
 * @param predicate Indicates whether the given candidate should be replaced by the result of
 *   [replacement].
 */
fun <T> Collection<T>.replacingBy(replacement: T.() -> T, predicate: (T) -> Boolean): List<T> {
  return toMutableList().apply { replaceBy(replacement, predicate) }.toList()
}

/**
 * Returns a [List] containing exactly the same elements of this [Collection], except for the ones
 * that match the [predicate] and have been replaced by the result of [replacement].
 *
 * @param replacement Lambda that receives the candidate being currently iterated and returns the
 *   replacement of the one that matches the [predicate].
 * @param predicate Indicates whether the given candidate should be replaced by the result of
 *   [replacement].
 * @throws IllegalStateException If multiple elements match the [predicate].
 */
internal fun <T> Collection<T>.replacingOnceBy(
  replacement: T.() -> T,
  predicate: (T) -> Boolean
): List<T> {
  return toMutableList().apply { replaceOnceBy(replacement, predicate) }.toList()
}
