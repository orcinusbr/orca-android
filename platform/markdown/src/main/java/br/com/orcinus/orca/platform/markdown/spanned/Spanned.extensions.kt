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

package br.com.orcinus.orca.platform.markdown.spanned

import android.content.Context
import android.text.Spanned
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.core.text.getSpans
import br.com.orcinus.orca.platform.markdown.spanned.span.areStructurallyEqual
import br.com.orcinus.orca.platform.markdown.spanned.span.toSpanStyle

/**
 * Obtains all of the [Part]s by which this [Spanned] is composed.
 *
 * @param context [Context] with which each spanned [Part] can compare its spans structurally.
 * @see areStructurallyEqual
 */
fun Spanned.getParts(context: Context): List<Part> {
  return mergeSpansIntoParts(context).fold(emptyList()) { accumulator, part ->
    if (accumulator.isNotEmpty() && part.indices.first > accumulator.last().indices.last.inc()) {
      accumulator + Part(accumulator.last().indices.last.inc()..part.indices.first.dec()) + part
    } else {
      accumulator + part
    }
  }
}

/**
 * Converts this [Spanned] into an [AnnotatedString].
 *
 * @param context [Context] with which each of this [Spanned]'s [Part]s can compare its spans
 *   structurally and conversions from spans into [SpanStyle]s will be performed.
 * @throws NoSuchFieldException If the one of the spans is an `androidx.compose.ui:ui-text`
 *   `DrawStyleSpan` but doesn't have a declared member property to which a [DrawStyle] is assigned.
 * @see Spanned.getParts
 * @see Spanned.getSpans
 * @see areStructurallyEqual
 */
@Throws(NoSuchFieldException::class)
internal fun Spanned.toAnnotatedString(context: Context): AnnotatedString {
  return buildAnnotatedString {
    append("${this@toAnnotatedString}")
    getParts(context).filterIsInstance<Part.Spanned>().forEach { part ->
      part.spans
        .map { span -> span.toSpanStyle(context) }
        .forEach { spanStyle -> addStyle(spanStyle, part.indices.first, part.indices.last) }
    }
  }
}

/**
 * Merges all spans that have been applied to this [Spanned] into ordered spanned [Part]s, from
 * which the indices at which they are can be obtained.
 *
 * @param context [Context] with which each spanned [Part] can compare its spans structurally.
 * @see Part.Spanned
 * @see Part.Spanned.getIndices
 * @see areStructurallyEqual
 */
private fun Spanned.mergeSpansIntoParts(context: Context): List<Part.Spanned> {
  return getSpans<Any>().map { Part(getSpanStart(it)..getSpanEnd(it).dec()).span(context, it) }
}
