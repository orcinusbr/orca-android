@file:JvmName("AnyExtensions")

package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import android.text.ParcelableSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan

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
