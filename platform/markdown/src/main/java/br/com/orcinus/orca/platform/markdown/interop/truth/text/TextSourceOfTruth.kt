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

package br.com.orcinus.orca.platform.markdown.interop.truth.text

import android.text.Editable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.platform.markdown.OnPostTextChangeListener
import br.com.orcinus.orca.platform.markdown.annotated.toAnnotatedString
import br.com.orcinus.orca.platform.markdown.annotated.toEditableAsState
import br.com.orcinus.orca.platform.markdown.interop.InteropEditText
import br.com.orcinus.orca.platform.markdown.interop.truth.SourceOfTruth
import br.com.orcinus.orca.platform.markdown.spanned.toMarkdown
import br.com.orcinus.orca.std.markdown.Markdown

/**
 * [SourceOfTruth] for accepting matching modifications and automatically undoing distinct ones that
 * have been rejected by the [onTextChange] callback (that is, weren't applied to the currently
 * passed in [text]).
 *
 * @property text Current [Markdown] on which changes can be performed.
 * @property onTextChange Lambda invoked whenever there's an attempt to modify the [text] and that
 *   defines whether or not it'll actually be changed.
 * @property style Converts an inserted [String] into [Markdown].
 * @property view [InteropEditText] whose textual alterations are to be approved by the callback and
 *   possibly reflected on the [text] resulted from a recomposition.
 * @property colors [Colors] that color the [AnnotatedString]s into which input [Editable]s are
 *   converted.
 */
private class TextSourceOfTruth(
  private val text: Markdown,
  private val onTextChange: (text: Markdown) -> Unit,
  private val style: (text: CharSequence) -> Markdown,
  private val view: InteropEditText,
  private val colors: Colors
) : SourceOfTruth {
  /** [OnTextChangeListener] that triggers the [onTextChange] callback. */
  private val onChangeListener = OnTextChangeListener {
    if ("$text" != it) {
      onTextChange(style(it))
    }
  }

  /**
   * [OnPostTextChangeListener] responsible for resetting the [view] to its state prior to the
   * current recomposition.
   */
  private val onPostChangeListener =
    object : OnPostTextChangeListener {
      override fun onPostTextChange(
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") changedText: Editable
      ) {
        val markedDownChangedText = changedText.toMarkdown(view.context)
        if (markedDownChangedText != text) {
          view.removeTextChangedListener(this)
          view.text = text.toAnnotatedString(colors).toEditableAsState(view.context).value
          view.addTextChangedListener(this)
        }
      }
    }

  override fun attach() {
    view.addTextChangedListener(onChangeListener)
    view.addTextChangedListener(onPostChangeListener)
  }

  override fun detach() {
    view.removeTextChangedListener(onPostChangeListener)
    view.removeTextChangedListener(onChangeListener)
  }
}

/**
 * Remembers a [SourceOfTruth] which accepts matching modifications and automatically undoes
 * distinct ones that have been rejected by the [onTextChange] callback (that is, weren't applied to
 * the currently passed in [text]).
 *
 * @param text Current [Markdown] on which changes can be performed.
 * @param onTextChange Lambda invoked whenever there's an attempt to modify the [text] and that
 *   defines whether or not it'll actually be changed.
 * @param style Converts an inserted [String] into [Markdown].
 * @param view [InteropEditText] whose textual alterations are to be approved by the callback and
 *   possibly reflected on the [text] resulted from a recomposition.
 * @param colors [Colors] that color the [AnnotatedString]s into which input [Editable]s are
 *   converted.
 */
@Composable
internal fun rememberSourceOfTruth(
  text: Markdown,
  onTextChange: (text: Markdown) -> Unit,
  style: (text: CharSequence) -> Markdown,
  view: InteropEditText,
  colors: Colors
): SourceOfTruth {
  return remember { TextSourceOfTruth(text, onTextChange, style, view, colors) }
}
