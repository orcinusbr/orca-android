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

package com.jeanbarrossilva.orca.std.injector.processor.inject

import com.google.devtools.ksp.symbol.Visibility

/**
 * Returns the [Visibility] that's least visible.
 *
 * @param a [Visibility] to be compared to [b].
 * @param b [Visibility] to be compared to [a].
 */
internal fun minOf(a: Visibility, b: Visibility): Visibility {
  val aWeight = a.weight()
  val bWeight = b.weight()
  val minWeight = listOf(aWeight, bWeight).min()
  return if (minWeight == aWeight) a else b
}

/**
 * Since the [Visibility] enum entries aren't sorted according to their "weight", returns a positive
 * [Int] that classifies this [Visibility].
 */
private fun Visibility.weight(): Int {
  return when (this) {
    Visibility.LOCAL -> 0
    Visibility.PRIVATE -> 1
    Visibility.PROTECTED -> 2
    Visibility.JAVA_PACKAGE -> 3
    Visibility.INTERNAL -> 4
    Visibility.PUBLIC -> 5
  }
}
