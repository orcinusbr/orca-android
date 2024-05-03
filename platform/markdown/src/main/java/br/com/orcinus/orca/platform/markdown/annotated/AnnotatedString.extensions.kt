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
import android.text.style.TextAppearanceSpan
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.intl.LocaleList
import br.com.orcinus.orca.platform.markdown.spanned.span.toSpanStyle
import kotlin.reflect.full.primaryConstructor

/**
 * Converts this [AnnotatedString] into a [State] that holds an [Editable].
 *
 * **NOTE**: Obtaining spans may result in some [Exception]s getting thrown depending on how the
 * [SpanStyle]s of the receiver were constructed:
 * - [IllegalArgumentException]: when a [SpanStyle] specifies a [Brush] that isn't a [SolidColor]
 *   nor a [ShaderBrush], since there aren't equivalent spans for [Brush]es other than those of such
 *   types; font feature settings, letter spacing, a [LocaleList] or a [Shadow] has been defined but
 *   font size hasn't; or its [FontStyle] is neither normal nor italic.
 * - [NoSuchFieldException]: when system version is at least Upside-Down Cake (API level 34), one of
 *   the [SpanStyle]s' font-specific values ([SpanStyle.fontWeight], [SpanStyle.fontStyle],
 *   [SpanStyle.fontSynthesis], [SpanStyle.fontFamily]) is non-`null` and the property to which the
 *   specified font feature settings would be assigned of the resulting [TextAppearanceSpan] that it
 *   was converted into isn't found.
 * - [NoSuchMethodException]: when a [SpanStyle]'s specified [Brush] is a [ShaderBrush] or a
 *   [DrawStyle] has been defined and the primary constructor of `androidx.compose.ui:ui-text`'s
 *   `ShaderBrushSpan` or `DrawStyleSpan` isn't found when converting [SpanStyle]s into spans, given
 *   that they're referenced and called through reflection because both are APIs are internal to the
 *   module in which they've been declared as of 1.6.6.
 *
 * @param context [Context] with which conversions from [SpanStyle]s into spans are performed and
 *   vice-versa.
 * @see Editable.getSpans
 * @see AnnotatedString.spanStyles
 * @see SpanStyle.brush
 * @see SpanStyle.fontFeatureSettings
 * @see SpanStyle.letterSpacing
 * @see SpanStyle.localeList
 * @see SpanStyle.shadow
 * @see SpanStyle.fontStyle
 * @see FontStyle.Companion.Normal
 * @see FontStyle.Companion.Italic
 * @see SpanStyle.drawStyle
 * @see primaryConstructor
 * @see SpanStyle.toSpans
 * @see Any.toSpanStyle
 */
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
       * Alias for `getSpans(0, length(), Object.class)`, which obtains all of the spans that have
       * been set to this [Editable].
       *
       * @see getSpans
       */
      private val spans
        get() = getSpans(start = 0, end = length, Object::class.java)

      override fun getChars(start: Int, end: Int, dest: CharArray?, destoff: Int) {
        dest?.let {
          var index = destoff
          for (character in slice(start..end.dec())) {
            it[index++] = character
          }
        }
      }

      override fun <T : Any?> getSpans(start: Int, end: Int, type: Class<T>?): Array<T> {
        @Suppress("UNCHECKED_CAST")
        return if (start == end) {
          emptyArray<Any>()
        } else {
          spanStyles
            .filter { start >= it.start && end <= it.end }
            .flatMap { it.item.toSpans(context) }
            .run {
              type
                ?.takeUnless { it == Object::class.java }
                ?.let { _ -> filter { it::class.java == type } }
                ?: this
            }
            .toTypedArray<Any>()
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
        what?.let {
          value =
            AnnotatedString.Builder()
              .apply {
                append(text)
                addStyle(it.toSpanStyle(context), start, end)
                spanStyles.forEach { addStyle(it.item, it.start, it.end) }
                paragraphStyles.forEach { addStyle(it.item, it.start, it.end) }
              }
              .toAnnotatedString()
              .toEditableAsState(context)
              .value
        }
      }

      override fun removeSpan(what: Any?) {
        what?.let { span ->
          value =
            AnnotatedString.Builder()
              .apply {
                append(text)
                spanStyles
                  .filterNot { span in it.item.toSpans(context) }
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
