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
import android.text.style.StyleSpan
import android.text.style.URLSpan
import br.com.orcinus.orca.std.markdown.style.Style

/** Converts this [Style] into a span. */
internal fun Style.toSpan(): Any {
  return when (this) {
    is Style.Bold -> StyleSpan(Typeface.BOLD)
    is Style.Italic -> StyleSpan(Typeface.ITALIC)
    is Style.Link -> URLSpan("$uri")
  }
}
