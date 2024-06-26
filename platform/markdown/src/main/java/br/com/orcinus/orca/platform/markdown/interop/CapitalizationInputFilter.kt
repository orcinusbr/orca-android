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

import android.text.InputFilter
import android.text.Spannable
import android.text.Spanned
import android.text.TextUtils
import androidx.core.text.getSpans

/** [InputFilter] for performing capitalization. */
internal sealed class CapitalizationInputFilter : InputFilter {
  /**
   * Whether this [Char] indicate the end of the grammatical structure on which capitalization is
   * performed.
   */
  protected abstract val Char.isDelimiter: Boolean

  /** Capitalizes each sentence. */
  data object Sentence : CapitalizationInputFilter() {
    override val Char.isDelimiter: Boolean
      get() = this == '\n' || this == '.' || this == '…' || this == '!' || this == '?'
  }

  /** Capitalizes each word. */
  data object Word : CapitalizationInputFilter() {
    override val Char.isDelimiter: Boolean
      get() = !isLetterOrDigit()
  }

  override fun filter(
    source: CharSequence?,
    start: Int,
    end: Int,
    dest: Spanned?,
    dstart: Int,
    dend: Int
  ): CharSequence? {
    return source?.let { _ ->
      val lengthening = hashMapOf<Int, Int>()
      buildString {
          var isDelimiting = true
          for ((index, char) in source.withIndex()) {
            lateinit var toAppend: String
            if (isDelimiting) {
              toAppend = char.uppercase()
              lengthening[index] = toAppend.length
            } else {
              toAppend = char.toString()
            }
            isDelimiting = char.isWhitespace() || char.isDelimiter
            append(toAppend)
          }
        }
        .also { copySpans(source, start, end, dest, dstart, dend, filtering = it, lengthening) }
    }
  }

  /**
   * Copies to the [destination] the spans that have been applied to the [source].
   *
   * @param source Text whose portion within [[sourceStartIndex], [sourceEndIndex]) will replace
   *   that of the [destination], whose is delimited by [[destinationStartIndex],
   *   [destinationEndIndex]).
   * @param sourceStartIndex Index that determines the beginning of the substring from the [source],
   *   by which the [destination]'s (constrained by its respective specified indices) will be
   *   replaced.
   * @param sourceEndIndex Index that determines the end of the substring from the [source], by
   *   which the [destination]'s (constrained by its respective specified indices) will be replaced.
   * @param destination Text whose portion within [[destinationStartIndex], [destinationEndIndex])
   *   will be replaced by that of the [source], whose is delimited by [[sourceStartIndex],
   *   [sourceEndIndex]).
   * @param destinationStartIndex Index that determines the beginning of the substring from the
   *   [destination] to be replaced by the [source]'s (constrained by its respective specified
   *   indices).
   * @param destinationEndIndex Index that determines the end of the substring from the
   *   [destination] to be replaced by the [source]'s (constrained by its respective specified
   *   indices).
   * @param filtering [String] resulted from the filtering process of a specific
   *   [CapitalizationInputFilter] implementation.
   * @param lengthening [HashMap] containing the indices at which capitalized [Char]s are in the
   *   [source] linked to the length of the [String] produced by having performed capitalization on
   *   them.
   */
  private fun copySpans(
    source: CharSequence,
    sourceStartIndex: Int,
    sourceEndIndex: Int,
    destination: Spanned?,
    destinationStartIndex: Int,
    destinationEndIndex: Int,
    filtering: String,
    lengthening: HashMap<Int, Int>
  ) {
    if (source is Spanned && destination is Spannable) {
      if (source.length == filtering.length) {
        val destinationOffset = 0
        TextUtils.copySpansFrom(
          source,
          sourceStartIndex,
          sourceEndIndex,
          source::class.java,
          destination,
          destinationOffset
        )
      } else {
        for ((index, length) in lengthening.entries) {
          val spans = source.getSpans<Any>(index, index.inc())
          for (span in spans) {
            val spanStartIndex =
              minOf(destinationStartIndex, source.getSpanStart(span)) - destinationStartIndex
            val spanEndIndex =
              minOf(destinationEndIndex, source.getSpanEnd(span) + length) - destinationEndIndex
            val flags = 0
            destination.setSpan(span, spanStartIndex, spanEndIndex, flags)
          }
        }
      }
    }
  }
}
