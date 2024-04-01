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

package com.jeanbarrossilva.orca.std.styledstring.style

import java.io.Serializable

/** Indicates where and how a target has been stylized. */
interface Style : Serializable {
  /** Indices at which both the style symbols and the target are in the whole [String]. */
  val indices: IntRange

  /** [Style] that requires its targets to match the [regex]. */
  abstract class Constrained : Style {
    /** [Regex] that targets to which this [Style] is applied should match. */
    internal abstract val regex: Regex

    /**
     * [IllegalArgumentException] thrown if the target is an invalid one for this [Style].
     *
     * @param target Target that's invalid.
     */
    inner class InvalidTargetException internal constructor(target: String) :
      IllegalArgumentException("Target doesn't match regex ($regex): \"$target\".")
  }

  /**
   * Copies this [Style].
   *
   * @param indices Indices at which both the style symbols and the target are in the whole
   *   [String].
   */
  fun at(indices: IntRange): Style
}
