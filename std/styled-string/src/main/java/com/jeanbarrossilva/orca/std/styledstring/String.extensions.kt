/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.std.styledstring

/**
 * Applies [transform] to each portion of this [String] that matches the [regex].
 *
 * @param regex [Regex] of the portion to be transformed.
 * @param transform Transformation to be performed on the [regex]-matching [String].
 */
internal fun <T> String.map(
  regex: Regex,
  transform: (indices: IntRange, match: String) -> T
): List<T> {
  return regex.findAll(this).map { transform(it.range, it.value) }.toList()
}
