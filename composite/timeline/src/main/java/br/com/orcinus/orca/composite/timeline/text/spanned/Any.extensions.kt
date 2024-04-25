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

@file:JvmName("AnyExtensions")

package br.com.orcinus.orca.composite.timeline.text.spanned

import android.text.ParcelableSpan
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.URLSpan
import br.com.orcinus.orca.std.markdown.style.Style
import java.net.URI

/**
 * Compares both spans structurally.
 *
 * @param other Span to be compared to this one.
 */
internal fun Any.isStructurallyEqualTo(other: Any): Boolean {
  return when {
    this is StyleSpan && other is StyleSpan -> isStructurallyEqualTo(other)
    this is URLSpan && other is URLSpan -> url == other.url
    this is ParcelableSpan && other is ParcelableSpan -> spanTypeId == other.spanTypeId
    else -> equals(other)
  }
}

/**
 * Converts this span into [Style]s.
 *
 * @param indices Indices of a [Spanned] to which this span has been applied.
 * @throws IllegalArgumentException If this is a [URLSpan] and the amount of indices doesn't match
 *   the length of the URL.
 * @see URLSpan.getURL
 */
internal fun Any.toStyles(indices: IntRange): List<Style> {
  return when (this) {
    is StyleSpan -> toStyles(indices)
    is URLSpan -> listOf(Style.Link.to(URI(url), indices))
    else -> emptyList()
  }
}
