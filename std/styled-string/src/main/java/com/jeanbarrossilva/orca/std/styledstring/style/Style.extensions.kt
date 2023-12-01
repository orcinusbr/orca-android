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
