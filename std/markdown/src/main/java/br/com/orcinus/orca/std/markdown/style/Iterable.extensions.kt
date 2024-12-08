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

package br.com.orcinus.orca.std.markdown.style

import kotlin.reflect.KClass

/**
 * Combines [Style]s whose indices are consecutive.
 *
 * @see Style.indices
 */
fun Iterable<Style>.merge(): List<Style> {
  return sortedBy { it.indices.first }
    .fold<_, Map<KClass<Style>, Set<Style>>?>(null) { accumulator, style ->
      @Suppress("UNCHECKED_CAST")
      (accumulator?.run {
        this +
          (style::class to
            (get(style::class)
              ?.map(Style::indices)
              ?.filter { it.last == style.indices.first }
              ?.map { style.at(it.first..style.indices.last) }
              ?.toSet()
              ?: setOf(style)))
      }
        ?: mapOf(style::class to setOf(style)))
        as Map<KClass<Style>, Set<Style>>
    }
    ?.values
    ?.flatten()
    .orEmpty()
}

/**
 * Removes [Style]s applied to the given region, trimming the remaining ones' for them to fit into
 * it.
 *
 * @param removal Produces the region from which [Style]s are to be removed.
 * @see Style.indices
 */
internal operator fun Iterable<Style>.minus(removal: (indices: IntRange) -> IntRange): List<Style> {
  return mapNotNull {
    val exclusion = removal(it.indices)
    if (it.indices in exclusion) {
      it
        .`if`({ indices.first < exclusion.first }) { at(indices.first until exclusion.first) }
        .`if`({ indices.last > exclusion.last }) { at(exclusion.last.inc()..this.indices.last) }
        .`if`<Style?>({ this == null || indices.isEmpty() || indices in exclusion }) { null }
    } else {
      it
    }
  }
}
