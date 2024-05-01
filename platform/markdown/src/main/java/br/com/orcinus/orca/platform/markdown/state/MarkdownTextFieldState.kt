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

package br.com.orcinus.orca.platform.markdown.state

import android.text.ParcelableSpan
import android.widget.EditText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.orcinus.orca.platform.markdown.MarkdownTextField
import br.com.orcinus.orca.platform.markdown.span.toParcelableSpan
import br.com.orcinus.orca.std.markdown.style.Style

/**
 * State from which a [MarkdownTextField] can be stylized.
 *
 * @see setInitialStyles
 * @see toggle
 * @see rememberMarkdownTextFieldState
 */
class MarkdownTextFieldState internal constructor() {
  /**
   * Whether no changes have been made or the ones that have been are undone.
   *
   * @see reset
   */
  private var isInInitialState = true

  /**
   * [Style]s that have been pushed.
   *
   * @see toggle
   */
  internal var styles by mutableStateOf(emptyList<Style>())
    private set

  /**
   * Applies the [style] to or removes it from the text.
   *
   * @param style [Style] to be toggled.
   */
  fun toggle(style: Style) {
    if (style in styles) {
      styles -= style
    } else {
      styles += style
    }
    isInInitialState = false
  }

  /**
   * Sets the [Style]s to be applied initially.
   *
   * @param initialStyles [Style]s to be applied when none has yet been.
   */
  internal fun setInitialStyles(initialStyles: List<Style>) {
    if (isInInitialState) {
      styles = initialStyles
      isInInitialState = false
    }
  }

  /**
   * Sets the [ParcelableSpan]s into which applied [Style]s will be converted in the [editText].
   *
   * @param editText [EditText] in which the [ParcelableSpan]s will be set.
   * @see styles
   */
  internal fun span(editText: EditText) {
    styles
      .associate { it.indices to it.toParcelableSpan() }
      .forEach { (indices, span) ->
        editText.text.setSpan(span, indices.first, indices.last.inc(), 0)
      }
  }

  /**
   * Resets this [MarkdownTextFieldState] to its initial state, removing all of the [Style]s that
   * have been applied.
   */
  internal fun reset() {
    styles = emptyList()
    isInInitialState = true
  }
}
