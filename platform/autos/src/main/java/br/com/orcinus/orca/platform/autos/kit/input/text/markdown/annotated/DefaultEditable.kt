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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.annotated

import android.text.Editable
import android.text.InputFilter

/**
 * Provides a default implementation for [getChars], [getFilters], [setFilters] and overloads of
 * [replace] and its variations — the [append], [delete] and [insert] methods — according to the
 * description of their behavior in [Editable]'s documentation.
 */
internal abstract class DefaultEditable : Editable {
  /**
   * [InputFilter]s that have been set for constraining replacements.
   *
   * @see replace
   * @see getFilters
   * @see setFilters
   */
  private var filters = emptyArray<InputFilter>()

  final override fun getChars(start: Int, end: Int, dest: CharArray?, destoff: Int) {
    dest?.let {
      var index = destoff
      for (character in slice(start..end.dec())) {
        it[index++] = character
      }
    }
  }

  final override fun append(text: Char): Editable {
    return append("$text")
  }

  final override fun append(text: CharSequence?): Editable {
    return append(text, start = 0, end = length)
  }

  final override fun append(text: CharSequence?, start: Int, end: Int): Editable {
    return replace(length, length, text, start, end)
  }

  final override fun getFilters(): Array<InputFilter> {
    return filters
  }

  final override fun setFilters(filters: Array<out InputFilter>?) {
    @Suppress("UNCHECKED_CAST")
    this.filters = filters as Array<InputFilter>
  }

  final override fun clear() {
    delete(st = 0, en = length)
  }

  final override fun delete(st: Int, en: Int): Editable {
    return replace(st, en, "", 0, 0)
  }

  final override fun insert(where: Int, text: CharSequence?): Editable {
    return text?.let { insert(where, text, start = 0, end = it.length) } ?: this
  }

  final override fun insert(where: Int, text: CharSequence?, start: Int, end: Int): Editable {
    return replace(where, where, text, start, end)
  }

  final override fun replace(st: Int, en: Int, text: CharSequence?): Editable {
    return text?.length?.let { replace(st, en, text, 0, it) } ?: this
  }

  final override fun replace(
    st: Int,
    en: Int,
    source: CharSequence?,
    start: Int,
    end: Int
  ): Editable {
    return source
      ?.substring(start, end)
      ?.let { filters.fold(it) { a, f -> f.filter(a, start, end, this, st, en)?.toString() ?: a } }
      ?.let { replaceRange(st, en, it) }
      ?.let(::replace)
      ?: this
  }

  /**
   * Replaces this [Editable]'s text by the given [replacement].
   *
   * @param replacement [CharSequence] to be set as the current text.
   */
  abstract fun replace(replacement: CharSequence): Editable
}
