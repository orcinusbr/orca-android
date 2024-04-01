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

package com.jeanbarrossilva.orca.ext.coroutines.replacement

/**
 * Returns a [List] containing exactly the same elements of this [Collection], except for the ones
 * that match the [predicate] and have been replaced by the result of [replacement].
 *
 * @param replacement Lambda that receives the candidate being currently iterated and returns the
 *   replacement of the one that matches the [predicate].
 * @param predicate Indicates whether the given candidate should be replaced by the result of
 *   [replacement].
 * @return Whether an element matching the [predicate] has been found and replaced.
 * @throws IllegalStateException If multiple elements match the [predicate].
 */
fun <T> MutableList<T>.replaceOnceBy(replacement: T.() -> T, predicate: (T) -> Boolean): Boolean {
  var replaced: T? = null
  replaceBy(replacement) {
    val isMatch = predicate(it)
    if (isMatch) {
      if (replaced == null) {
        replaced = it
      } else {
        throw IllegalStateException("Multiple predicate matches: $replaced and $it.")
      }
    }
    isMatch
  }
  return replaced != null
}

/**
 * Replaces the elements that match the [predicate] by the result of [replacement].
 *
 * @param replacement Lambda that receives the candidate being currently iterated and returns the
 *   replacement of the one that matches the [predicate].
 * @param predicate Indicates whether the given candidate should be replaced by the result of
 *   [replacement].
 */
private fun <T> MutableList<T>.replaceBy(replacement: T.() -> T, predicate: (T) -> Boolean) {
  replaceAll { if (predicate(it)) it.replacement() else it }
}
