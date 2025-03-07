/*
 * Copyright © 2025 Orcinus
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

package br.com.orcinus.orca.ext.functional

/**
 * Transforms each element in the [Iterable] of this [Result] and combines the produced [Result]s
 * into a single one. That which is returned by this method holds either all successful
 * transformations or the [Exception] thrown by the first failing one.
 *
 * @param T Value resulted from this [Result].
 * @param R Value resulted from a transformation.
 * @param transform Transforms an element of the value of the receiver [Result] into another
 *   [Result].
 */
inline fun <T, R> Result<Iterable<T>>.onEach(transform: (T) -> Result<R>) = flatMap { elements ->
  runCatching { elements.map { element -> transform(element).getOrThrow() } }
}

/**
 * Returns the [Result] resulted from the transformation in case it is not a failure; otherwise, the
 * receiver one is cast to `Result<R>` and returned (which is safe because its successful value is
 * nonexistent and, thus, unobtainable).
 *
 * @param T Value resulted from this [Result].
 * @param R Value resulted from a transformation.
 * @param transform Transforms the value of the receiver [Result] into another [Result].
 */
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>) = map(transform).flatten()

/**
 * Returns the [Result] resulted from this one in case this one is not a failure; otherwise, this
 * one is cast to `Result<R>` and returned (which is safe because its successful value is
 * nonexistent and, thus, unobtainable).
 *
 * @param R Value resulted from the nested [Result].
 */
@Suppress("UNCHECKED_CAST") fun <R> Result<Result<R>>.flatten() = getOrDefault(this as Result<R>)

/**
 * Erases information about the successful value provided by this [Result] by converting it into a
 * `Result<Unit>`. Intended for scenarios in which an internal context should not be leaked to
 * consumers, functionally indicating to them only that obtaining the value _may_ throw.
 */
fun Result<*>.unit() = map {}
