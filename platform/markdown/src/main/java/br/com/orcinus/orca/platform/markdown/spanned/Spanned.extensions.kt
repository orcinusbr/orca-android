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
import androidx.core.text.getSpans
import br.com.orcinus.orca.platform.markdown.spanned.span.isStructurallyEqual
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style

/**
 * Converts this [Spanned] into [Markdown].
 *
 * @param context [Context] with which each of this [Spanned]'s [IndexedSpans] can compare its spans
 *   and their conversions into [Style]s will be performed.
 * @see Spanned.getIndexedSpans
 * @see isStructurallyEqual
 * @see IndexedSpans.toStyles
 */
fun Spanned.toMarkdown(context: Context): Markdown {
  val text = toString()
  val styles = getIndexedSpans(context).flatMap(IndexedSpans::toStyles)
  return Markdown.styled(text, styles)
}

/**
 * Obtains all of the spans by which this [Spanned] is composed, alongside the indices that they are
 * in.
 *
 * **NOTE**: Even though [IndexedSpans] themselves can contain no spans at all, portions to which
 * none have been applied are omitted.
 *
 * @param context [Context] with which [IndexedSpans] can compare its spans structurally.
 * @see isStructurallyEqual
 */
internal fun Spanned.getIndexedSpans(context: Context): List<IndexedSpans> {
  return getSpans<Any>().map { IndexedSpans(context, getSpanStart(it)..getSpanEnd(it).dec(), it) }
}
