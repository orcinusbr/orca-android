/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.platform.markdown.spanned.span

import android.graphics.Typeface
import android.os.Build
import android.text.style.StyleSpan

/**
 * Creates a [StyleSpan], defining its font weight adjustment if the version of Android currently
 * being used supports it.
 *
 * @param style One of the constants defined by [Typeface] that determines the style of the
 *   [StyleSpan].
 * @param fontWeightAdjustment Adjustment to be made to the supplied font weight.
 * @see StyleSpan.getFontWeightAdjustment
 */
internal fun createStyleSpan(style: Int, fontWeightAdjustment: Int): StyleSpan {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    StyleSpan(style, fontWeightAdjustment)
  } else {
    StyleSpan(style)
  }
}
