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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown

import android.text.Editable
import android.text.TextWatcher

/**
 * [TextWatcher] that doesn't perform any operations before or at the moment the text is changed,
 * but rather is notified only after the modification has been made (which differs from
 * [OnTextChangeListener]'s behavior of listening to the current input).
 *
 * @see onPostTextChange
 */
internal interface OnPostTextChangeListener : TextWatcher {
  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

  override fun afterTextChanged(s: Editable?) {
    s?.run(::onPostTextChange)
  }

  /**
   * Callback called after the text has changed.
   *
   * @param text [Editable] by which the previous one was replaced.
   */
  fun onPostTextChange(text: Editable)
}
