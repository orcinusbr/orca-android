/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
