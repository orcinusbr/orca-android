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

package com.jeanbarrossilva.orca.std.buildable.processor

import java.util.Locale

/** Capitalizes this [String]. */
internal fun String.capitalize(): String {
  val locale = Locale.getDefault()
  return replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
}

/**
 * Replaces the last occurrence of the [replaced] by the [replacement].
 *
 * @param replaced [String] to be replaced.
 * @param replacement [String] to replace the [replaced].
 */
internal fun String.replaceLast(replaced: String, replacement: String): String {
  val lastIndex = lastIndexOf(replaced)
  val containsOldValue = lastIndex != -1
  return if (containsOldValue) replaceRange(lastIndex..lastIndex + replaced.lastIndex, replacement)
  else this
}
