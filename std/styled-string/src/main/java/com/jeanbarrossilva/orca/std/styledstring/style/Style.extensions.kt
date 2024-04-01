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

/**
 * Returns whether this [Style] is chopped (that is, starts but overflows) by the [text].
 *
 * @param text [String] that may chop this [Style].
 */
internal fun Style.isChoppedBy(text: String): Boolean {
  return indices.first < text.length && indices.last > text.lastIndex
}

/**
 * Returns whether this [Style] is within the [text]'s bounds.
 *
 * @param text [String] in which this [Style] may be present.
 */
internal fun Style.isWithin(text: String): Boolean {
  return indices.first >= 0 && indices.last < text.length
}
