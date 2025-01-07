/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.composition.interop

import br.com.orcinus.orca.std.markdown.style.Style
import br.com.orcinus.orca.std.markdown.style.contains
import br.com.orcinus.orca.std.markdown.style.`if`

/**
 * Performs the inverse of [br.com.orcinus.orca.std.markdown.style.minus] by constraining the
 * [Style]s to the given region.
 *
 * @param constraint Region to which the [Style]s are to be constrained.
 */
@PublishedApi
internal operator fun Iterable<Style>.rem(constraint: IntRange): List<Style> {
  return mapNotNull {
    if (it.indices in constraint) {
      it
        .`if`({ indices.first <= constraint.first }) { at(constraint.first..indices.last) }
        .`if`({ indices.last >= constraint.last }) { at(indices.first..constraint.last) }
        .`if`<Style?>({ this == null || indices == constraint }) { null }
    } else {
      it
    }
  }
}
