@file:JvmName("AnyExtensions")

package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import android.text.ParcelableSpan
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.URLSpan
import com.jeanbarrossilva.orca.std.styledstring.Style
import com.jeanbarrossilva.orca.std.styledstring.type.Link
import java.net.URL

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
    is URLSpan -> listOf(Link.to(URL(url), indices))
    else -> emptyList()
  }
}
