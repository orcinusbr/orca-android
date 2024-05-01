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

package br.com.orcinus.orca.composite.timeline.text.annotated.span

import android.text.ParcelableSpan
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.URLSpan
import br.com.orcinus.orca.std.markdown.style.Style
import java.net.URI

/**
 * Converts this [ParcelableSpan] into [Style]s.
 *
 * @param indices Indices of a [Spanned] to which this [ParcelableSpan] has been applied.
 * @throws IllegalArgumentException If this is a [URLSpan] and the amount of indices doesn't match
 *   the length of the URL.
 * @see URLSpan.getURL
 */
internal fun ParcelableSpan.toStyles(indices: IntRange): List<Style> {
  return when (this) {
    is StyleSpan -> toStyles(indices)
    is URLSpan -> listOf(Style.Link(URI(url), indices))
    else -> emptyList()
  }
}

/**
 * Converts this [StyleSpan] into [Style]s.
 *
 * @param indices Indices of a [Spanned] to which this [StyleSpan] has been applied.
 */
private fun StyleSpan.toStyles(indices: IntRange): List<Style> {
  return when (style) {
    android.graphics.Typeface.BOLD -> listOf(Style.Bold(indices))
    android.graphics.Typeface.BOLD_ITALIC -> listOf(Style.Bold(indices), Style.Italic(indices))
    android.graphics.Typeface.ITALIC -> listOf(Style.Italic(indices))
    else -> emptyList()
  }
}
