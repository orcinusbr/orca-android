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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition.markdown

import android.content.Context
import android.text.Spanned
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.span.isStructurallyEqual
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.span.toSpan
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style

/**
 * Converts this [Markdown] into a [Spanned].
 *
 * @param context [Context] with which [Style]s are converted into spans.
 * @see Style.toSpan
 */
internal fun Markdown.toSpanned(context: Context): Spanned {
  return object : Spanned, CharSequence by this {
    override fun <T : Any?> getSpans(start: Int, end: Int, type: Class<T>?): Array<T> {
      @Suppress("UNCHECKED_CAST")
      return type
        ?.let { _ ->
          styles
            .filter { it.indices.first >= start && it.indices.last < end }
            .map(Style::toSpan)
            .filterIsInstance(type as Class<T & Any>)
            .toTypedArray<Any>()
        }
        .orEmpty() as Array<T>
    }

    override fun getSpanStart(tag: Any?): Int {
      return styles
        .takeIf { tag != null }
        ?.associateWith { it.indices.first }
        ?.filterKeys { it.toSpan().isStructurallyEqual(context, tag!!) }
        ?.values
        ?.lastOrNull()
        ?: -1
    }

    override fun getSpanEnd(tag: Any?): Int {
      return styles
        .takeIf { tag != null }
        ?.associateWith { it.indices.last }
        ?.filterKeys { it.toSpan().isStructurallyEqual(context, tag!!) }
        ?.values
        ?.lastOrNull()
        ?.inc()
        ?: -1
    }

    override fun getSpanFlags(tag: Any?): Int {
      return 0
    }

    override fun nextSpanTransition(start: Int, limit: Int, type: Class<*>?): Int {
      return type?.let { _ ->
        val spans = getSpans(start = start.inc(), end = limit, type)
        if (start <= spans.lastIndex) {
          spans
            .drop(start)
            .withIndex()
            .find { it.index < spans.lastIndex && spans[it.index.inc()]::class != it.value::class }
            ?.index
            ?: limit
        } else {
          limit
        }
      }
        ?: limit
    }
  }
}
