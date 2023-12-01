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

import android.graphics.Typeface
import android.os.Build
import android.text.Spanned
import android.text.style.StyleSpan
import com.jeanbarrossilva.orca.std.styledstring.style.Style
import com.jeanbarrossilva.orca.std.styledstring.style.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.style.type.Italic

/**
 * Creates a [StyleSpan], defining its font weight adjustment if the version of Android currently
 * being used supports it.
 *
 * @param style One of the constants defined by [Typeface] that determines the style of the
 *   [StyleSpan].
 * @param fontWeightAdjustment Adjustment to be made to the supplied font weight.
 * @see StyleSpan.getFontWeightAdjustment
 */
internal fun StyleSpan(style: Int, fontWeightAdjustment: Int): StyleSpan {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    StyleSpan(style, fontWeightAdjustment)
  } else {
    StyleSpan(style)
  }
}

/**
 * Whether this [StyleSpan] is structurally equal to the [other].
 *
 * @param other [StyleSpan] to which this one will be structurally compared.
 */
internal fun StyleSpan.isStructurallyEqualTo(other: StyleSpan): Boolean {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    style == other.style && fontWeightAdjustment == other.fontWeightAdjustment
  } else {
    style == other.style
  }
}

/**
 * Converts this [StyleSpan] into [Style]s.
 *
 * @param indices Indices of a [Spanned] to which this [StyleSpan] has been applied.
 */
internal fun StyleSpan.toStyles(indices: IntRange): List<Style> {
  return when (style) {
    Typeface.BOLD -> listOf(Bold(indices))
    Typeface.BOLD_ITALIC -> listOf(Bold(indices), Italic(indices))
    Typeface.ITALIC -> listOf(Italic(indices))
    else -> emptyList()
  }
}
