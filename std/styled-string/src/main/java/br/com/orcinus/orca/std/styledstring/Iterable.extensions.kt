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

package br.com.orcinus.orca.std.styledstring

/**
 * Applies the given [transform] to the currently iterated element if its [predicate] returns
 * `true`.
 *
 * @param I Element of the [Iterable] being conditionally mapped.
 * @param O Element of the resulting [List].
 * @param predicate Returns whether the element should be transformed.
 * @param transform Transformation to be performed on the element.
 */
internal fun <I, O : I> Iterable<I>.map(predicate: (I) -> Boolean, transform: (I) -> O): List<I> {
  return map { if (predicate(it)) transform(it) else it }
}
