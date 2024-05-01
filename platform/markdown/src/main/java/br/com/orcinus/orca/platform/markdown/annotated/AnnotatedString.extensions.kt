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

package br.com.orcinus.orca.platform.markdown.annotated

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.ParcelableSpan
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import br.com.orcinus.orca.platform.markdown.spanned.span.toSpanStyle
import kotlin.reflect.full.primaryConstructor

/**
 * Converts this [AnnotatedString] into a [State] that holds an [Editable].
 *
 * @param context [Context] with which conversions from [ParcelableSpan]s into [SpanStyle]s are
 *   performed.
 * @throws IllegalArgumentException If any of the [SpanStyle]s specifies a [Brush] isn't a
 *   [SolidColor] nor a [ShaderBrush], since there aren't equivalent [ParcelableSpan]s for [Brush]es
 *   other than those of such types.
 * @throws NoSuchMethodException If the specified [Brush] is a [ShaderBrush] or a [DrawStyle] has
 *   been defined and the primary constructor of `androidx.compose.ui:ui-text`'s `DrawStyleSpan` or
 *   `ShaderBrushSpan` isn't found when converting [SpanStyle]s into [ParcelableSpan]s, given that
 *   they're referenced and called through reflection because both are APIs are internal to the
 *   module in which they've been declared as of 1.6.6.
 * @see ParcelableSpan.toSpanStyle
 * @see AnnotatedString.spanStyles
 * @see SpanStyle.brush
 * @see SpanStyle.drawStyle
 * @see primaryConstructor
 */
@Throws(IllegalArgumentException::class, NoSuchMethodException::class)
internal fun AnnotatedString.toEditableAsState(context: Context): State<Editable> {
  return buildState {
    object : Editable, CharSequence by text {
      /**
       * [InputFilter]s that have been set for constraining changes made to this [Editable].
       *
       * @see setFilters
       * @see getFilters
       */
      private var filters = emptyArray<InputFilter>()

      /**
       * Alias for `getSpans(0, length(), ParcelableSpan.class)`, which obtains all of the
       * [ParcelableSpan]s that have been set to this [Editable] (which is the only type of span
       * that it can contain).
       *
       * @see getSpans
       */
      private val spans
        get() = getSpans(start = 0, end = length, ParcelableSpan::class.java)

      override fun getChars(start: Int, end: Int, dest: CharArray?, destoff: Int) {
        dest?.let {
          var index = destoff
          for (character in slice(start..end.dec())) {
            it[index++] = character
          }
        }
      }

      override fun <T : Any?> getSpans(start: Int, end: Int, type: Class<T>?): Array<T> {
        val spanCount = spanStyles.size
        val coercedStart = start.coerceIn(0, spanCount)
        val coercedEnd = end.coerceIn(0, spanCount)

        @Suppress("UNCHECKED_CAST")
        return if (coercedStart == 0 && coercedEnd == 0) {
          emptyArray<ParcelableSpan>()
        } else {
          spanStyles
            .subList(coercedStart, coercedEnd)
            .flatMap { it.item.toParcelableSpans() }
            .run { type?.let { _ -> filter { it::class.java == type } } ?: this }
            .toTypedArray<ParcelableSpan>()
        }
          as Array<T>
      }

      override fun getSpanStart(tag: Any?): Int {
        return tag?.let { _ -> spans.indexOfFirst { it == tag } } ?: -1
      }

      override fun getSpanEnd(tag: Any?): Int {
        return tag?.let { _ -> spans.indexOfLast { it == tag } } ?: -1
      }

      override fun getSpanFlags(tag: Any?): Int {
        return 0
      }

      override fun nextSpanTransition(start: Int, limit: Int, type: Class<*>?): Int {
        return if (start <= spans.lastIndex) {
          spans
            .withIndex()
            .drop(start.inc())
            .find { it.index < spans.lastIndex && spans[it.index.inc()]::class != it.value::class }
            ?.index
            ?: limit
        } else {
          limit
        }
      }

      override fun setSpan(what: Any?, start: Int, end: Int, flags: Int) {
        if (what is ParcelableSpan) {
          value =
            AnnotatedString.Builder()
              .apply {
                append(text)
                addStyle(what.toSpanStyle(context), start, end)
                spanStyles.forEach { addStyle(it.item, it.start, it.end) }
                paragraphStyles.forEach { addStyle(it.item, it.start, it.end) }
              }
              .toAnnotatedString()
              .toEditableAsState(context)
              .value
        }
      }

      override fun removeSpan(what: Any?) {
        if (what is ParcelableSpan) {
          value =
            AnnotatedString.Builder()
              .apply {
                append(text)
                spanStyles
                  .filterNot { what in it.item.toParcelableSpans() }
                  .forEach { addStyle(it.item, it.start, it.end) }
                paragraphStyles.forEach { addStyle(it.item, it.start, it.end) }
              }
              .toAnnotatedString()
              .toEditableAsState(context)
              .value
        }
      }

      override fun append(text: Char): Editable {
        return append("$text")
      }

      override fun append(text: CharSequence?): Editable {
        return append(text, start = 0, end = length)
      }

      override fun append(text: CharSequence?, start: Int, end: Int): Editable {
        return replace(st = length, en = length, text, start, end)
      }

      override fun getFilters(): Array<InputFilter> {
        return filters
      }

      override fun setFilters(filters: Array<out InputFilter>?) {
        @Suppress("UNCHECKED_CAST")
        this.filters = filters as Array<InputFilter>
      }

      override fun clear() {
        delete(st = 0, en = length)
      }

      override fun delete(st: Int, en: Int): Editable {
        return replace(st, en, source = "", start = 0, end = 0)
      }

      override fun insert(where: Int, text: CharSequence?): Editable {
        return text?.let { insert(where, text, 0, it.length) } ?: this
      }

      override fun insert(where: Int, text: CharSequence?, start: Int, end: Int): Editable {
        return replace(st = where, en = where, text, start, end)
      }

      override fun replace(st: Int, en: Int, text: CharSequence?): Editable {
        return text?.let { replace(st, en, source = it, 0, it.length) } ?: this
      }

      override fun replace(
        st: Int,
        en: Int,
        source: CharSequence?,
        start: Int,
        end: Int
      ): Editable {
        val replacementText = source?.substring(start, end) ?: return this
        val filteredText =
          filters.fold(replacementText) { accumulator, filter ->
            filter.filter(accumulator, start, end, this, st, en)?.toString() ?: accumulator
          }
        val replacedText = text.replaceRange(st, en, filteredText)
        return AnnotatedString(replacedText, spanStyles, paragraphStyles)
          .toEditableAsState(context)
          .value
      }

      override fun clearSpans() {
        value = AnnotatedString(text).toEditableAsState(context).value
      }
    }
  }
}
