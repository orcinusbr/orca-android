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

package br.com.orcinus.orca.platform.markdown

import android.text.InputFilter
import android.widget.EditText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.insertTextAtCursor
import androidx.compose.ui.semantics.requestFocus
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setText
import androidx.compose.ui.semantics.text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import br.com.orcinus.orca.platform.markdown.annotated.toEditableAsState
import br.com.orcinus.orca.platform.markdown.spanned.toAnnotatedString
import br.com.orcinus.orca.platform.markdown.state.MarkdownTextFieldState
import br.com.orcinus.orca.platform.markdown.state.rememberMarkdownTextFieldState
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.buildMarkdown
import br.com.orcinus.orca.std.markdown.style.Style
import org.jetbrains.annotations.VisibleForTesting

/** Tag that identifies a [MarkdownTextField] for testing purposes. */
@Stable const val MarkdownTextFieldTag = "markdown-text-field"

/**
 * [Markdown]-based text field with sample text and a no-op change listener.
 *
 * @param modifier [Modifier] that is applied to the underlying [AndroidView].
 */
@Composable
@VisibleForTesting
fun MarkdownTextField(modifier: Modifier = Modifier) {
  MarkdownTextField(
    rememberMarkdownTextFieldState(),
    text =
      buildMarkdown {
        italic { +"Hello" }
        +", "
        bold { +"world" }
        +'!'
      },
    onTextChange = {},
    modifier
  )
}

/**
 * Text field to which stylization can be applied from the provided [text].
 *
 * @param state [MarkdownTextFieldState] from which [Style]s can be toggled.
 * @param text [Markdown] to be displayed.
 * @param onTextChange Lambda that gets invoked whenever the text changes.
 * @param modifier [Modifier] that is applied to the underlying [AndroidView].
 */
@Composable
fun MarkdownTextField(
  state: MarkdownTextFieldState,
  text: Markdown,
  onTextChange: (text: Markdown) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val editText = remember(context) { EditText(context) }
  var styles by remember(state) { mutableStateOf(emptyList<Style>()) }
  val markDown: (String) -> Markdown by rememberUpdatedState { Markdown.styled(it, styles) }
  val onStylizationListener =
    remember(state) {
      MarkdownTextFieldState.OnStylizationListener {
        styles = it
        onTextChange(markDown("$text"))
      }
    }
  val onTextChangeListener = remember { OnTextChangeListener { onTextChange(markDown(it)) } }

  AndroidView(
    { editText },
    modifier.testTag(MarkdownTextFieldTag).semantics {
      editText.text?.toAnnotatedString(context)?.run { this@semantics.text = this }
      insertTextAtCursor {
        editText.text != editText.text?.append(it.toEditableAsState(context).value)
      }
      requestFocus(action = editText::requestFocus)
      setText { annotatedString ->
        annotatedString.toEditableAsState(context).value.let { editable ->
          editText.text = editable
          editText.text == editable
        }
      }
    },
    onRelease = {
      it.text?.clear()
      it.removeTextChangedListener(onTextChangeListener)
      state.reset()
    }
  ) {
    it.background = null
    it.addTextChangedListener(onTextChangeListener)
    it.text?.filters =
      arrayOf(
        InputFilter { source, _, _, _, _, _ ->
          onTextChange(text)
          if (source != "$text") text else null
        }
      )
    it.setText(text)
    state.setOnStylizationListener(onStylizationListener)
    state.setInitialStyles(text.styles)
    state.span(it)
  }
}

/** Preview of a [MarkdownTextField]. */
@Composable
@Preview
private fun MarkdownTextFieldPreview() {
  MarkdownTextField()
}
