/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.composition.interop

import android.text.Editable
import android.text.TextWatcher

/**
 * [TextWatcher] that requires only for a single method to be overridden, to which a non-`null`
 * [String] that has been defined as the current text is provided and from which operations based on
 * that modification can be performed.
 *
 * @see onTextChange
 */
internal fun interface OnTextChangeListener : TextWatcher {
  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    val text = s?.toString() ?: return
    onTextChange(text)
  }

  override fun afterTextChanged(s: Editable?) {}

  /**
   * Callback that is executed when the text changes, with the [CharSequence] that is provided to
   * [onTextChanged] converted into a [String] when it isn't `null`; in case it equals to `null`,
   * this method doesn't get called.
   *
   * @param text [String] that has been set as the current text.
   */
  fun onTextChange(text: String)
}
