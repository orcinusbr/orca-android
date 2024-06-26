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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.state

import android.widget.EditText
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.MarkdownTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.spanned.span.toSpan
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

  /** [OnStylizationListener] that will be notified of [Style] changes. */
  private var onStylizationListener: OnStylizationListener? = null

  /**
   * [Style]s that have been applied.
   *
   * @see toggle
   */
  var styles = emptyList<Style>()
    private set

  /**
   * Listener to be notified of changes in the [Style]s that have been applied.
   *
   * @see onStylization
   */
  fun interface OnStylizationListener {
    /**
     * Callback that gets called whenever stylization is performed.
     *
     * @param styles Currently applied [Style]s after the change.
     */
    fun onStylization(styles: List<Style>)
  }

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
    onStylizationListener?.onStylization(styles)
    isInInitialState = false
  }

  /**
   * Defines the [OnStylizationListener] that will be notified of [Style] changes.
   *
   * @param onStylizationListener [OnStylizationListener] to be defined.
   */
  internal fun setOnStylizationListener(onStylizationListener: OnStylizationListener) {
    this.onStylizationListener = onStylizationListener
  }

  /**
   * Sets the [Style]s to be applied initially.
   *
   * @param initialStyles [Style]s to be applied when none has yet been.
   */
  internal fun setInitialStyles(initialStyles: List<Style>) {
    if (isInInitialState) {
      styles = initialStyles
      onStylizationListener?.onStylization(styles)
      isInInitialState = false
    }
  }

  /**
   * Sets the spans into which applied [Style]s will be converted in the [editText].
   *
   * @param editText [EditText] in which the spans will be set.
   * @see styles
   */
  internal fun span(editText: EditText) {
    styles
      .associate { it.indices to it.toSpan() }
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
    onStylizationListener?.onStylization(styles)
    onStylizationListener = null
    isInInitialState = true
  }
}
