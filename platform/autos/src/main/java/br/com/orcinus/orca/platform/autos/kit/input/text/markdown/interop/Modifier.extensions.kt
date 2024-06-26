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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.focused
import androidx.compose.ui.semantics.insertTextAtCursor
import androidx.compose.ui.semantics.requestFocus
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setSelection
import androidx.compose.ui.semantics.setText
import androidx.compose.ui.semantics.text
import androidx.compose.ui.semantics.textSelectionRange
import androidx.compose.ui.text.TextRange
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.annotated.toEditableAsState
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop.truth.text.OnTextChangeListener

/**
 * Interoperates between Jetpack Compose and the [View] system by proxying semantics-related
 * operations requested to be performed on the [Composable] to the specified [view] (such as
 * focusing, text-setting, selecting, etc).
 *
 * @param view [InteropEditText] that will perform the actions that have been requested to be
 *   executed by the [Composable].
 * @see requestFocus
 * @see setText
 * @see setSelection
 */
internal fun Modifier.proxy(view: InteropEditText): Modifier {
  return semantics {
    val context = view.context ?: return@semantics
    val setText: () -> Unit = { view.text?.toAnnotatedString(context)?.let { text = it } }
    val onTextChangeListener = OnTextChangeListener { setText() }
    focused = view.isFocused
    textSelectionRange = TextRange(view.selectionStart, view.selectionEnd)
    view.setOnFocusChangeListener { _, hasFocus -> focused = hasFocus }
    view.addTextChangedListener(onTextChangeListener)
    view.addOnAttachStateChangeListener(
      object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {}

        override fun onViewDetachedFromWindow(v: View) {
          view.removeTextChangedListener(onTextChangeListener)
          v.removeOnAttachStateChangeListener(this)
        }
      }
    )
    insertTextAtCursor {
      val toInsert = it.toEditableAsState(context).value
      view.text != view.text?.append(toInsert)
    }
    requestFocus(action = view::requestFocus)
    setSelection { startIndex, endIndex, _ ->
      view.setSelection(startIndex, endIndex)
      true
    }
    setText()
    setText {
      val toSet = it.toEditableAsState(context).value
      view.text = toSet
      true
    }
  }
}
