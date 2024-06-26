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

package br.com.orcinus.orca.platform.markdown.interop

import android.content.Context
import android.text.Spanned
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import br.com.orcinus.orca.platform.markdown.spanned.IndexedSpans
import br.com.orcinus.orca.platform.markdown.spanned.getIndexedSpans
import br.com.orcinus.orca.platform.markdown.spanned.span.isStructurallyEqual
import br.com.orcinus.orca.platform.markdown.spanned.span.toSpanStyle

/**
 * Converts this [Spanned] into an [AnnotatedString].
 *
 * @param context [Context] with which each of this [Spanned]'s [IndexedSpans] can compare its spans
 *   structurally and conversions from spans into [SpanStyle]s will be performed.
 * @throws NoSuchFieldException If the one of the spans is an `androidx.compose.ui:ui-text`
 *   `DrawStyleSpan` but doesn't have a declared member property to which a [DrawStyle] is assigned.
 * @see Spanned.getIndexedSpans
 * @see Spanned.getSpans
 * @see isStructurallyEqual
 */
@Throws(NoSuchFieldException::class)
internal fun Spanned.toAnnotatedString(context: Context): AnnotatedString {
  return buildAnnotatedString {
    append("${this@toAnnotatedString}")
    getIndexedSpans(context).forEach { indexedSpans ->
      indexedSpans.spans
        .map { span -> span.toSpanStyle(context) }
        .forEach { spanStyle ->
          addStyle(spanStyle, indexedSpans.indices.first, indexedSpans.indices.last.inc())
        }
    }
  }
}
