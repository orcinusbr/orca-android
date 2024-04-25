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

package br.com.orcinus.orca.composite.timeline.text.spanned

import android.graphics.Typeface
import android.os.Build
import android.text.Spanned
import android.text.style.StyleSpan
import br.com.orcinus.orca.std.markdown.style.Style

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
    Typeface.BOLD -> listOf(Style.Bold(indices))
    Typeface.BOLD_ITALIC -> listOf(Style.Bold(indices), Style.Italic(indices))
    Typeface.ITALIC -> listOf(Style.Italic(indices))
    else -> emptyList()
  }
}
