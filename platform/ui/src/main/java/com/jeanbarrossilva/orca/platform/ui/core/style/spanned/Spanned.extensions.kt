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

package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import android.text.Spanned
import androidx.core.text.getSpans
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/** [Part]s by which this [Spanned] is composed. */
internal val Spanned.parts
  get() =
    mergeSpansIntoParts().fold(emptyList<Part>()) { accumulator, part ->
      if (accumulator.isNotEmpty() && part.indices.first > accumulator.last().indices.last.inc()) {
        accumulator + Part(accumulator.last().indices.last.inc()..part.indices.first.dec()) + part
      } else {
        accumulator + part
      }
    }

/** Converts this [Spanned] into a [StyledString]. */
fun Spanned.toStyledString(): StyledString {
  val text = toString()
  val styles = parts.filterIsInstance<Part.Spanned>().flatMap(Part.Spanned::toStyles)
  return StyledString(text, styles)
}

/**
 * Merges all spans that have been applied to this [Spanned] into ordered spanned [Part]s, from
 * which the spans and also their respective indices can be obtained.
 *
 * @see Part.Spanned
 * @see Part.Spanned.getIndices
 */
private fun Spanned.mergeSpansIntoParts(): List<Part.Spanned> {
  return getSpans<Any>().map { Part(getSpanStart(it)..getSpanEnd(it).dec()).span(it) }
}
