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

import android.text.Editable
import android.view.Gravity
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.platform.markdown.annotated.toAnnotatedString
import br.com.orcinus.orca.platform.markdown.annotated.toEditableAsState
import br.com.orcinus.orca.platform.markdown.interop.InteropEditText
import br.com.orcinus.orca.platform.markdown.interop.drawableStateOf
import br.com.orcinus.orca.platform.markdown.interop.proxy
import br.com.orcinus.orca.platform.markdown.interop.truth.text.rememberSourceOfTruth
import br.com.orcinus.orca.platform.markdown.state.MarkdownTextFieldState
import br.com.orcinus.orca.platform.markdown.state.rememberMarkdownTextFieldState
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style

/** Tag that identifies a [MarkdownTextField] for testing purposes. */
const val MarkdownTextFieldTag = "markdown-text-field"

/**
 * [Markdown]-based text field with a no-op change listener.
 *
 * @param modifier [Modifier] that is applied to the underlying [AndroidView].
 * @param value [MarkdownTextFieldValue] containing [Markdown] to be displayed.
 */
@Composable
@VisibleForTesting
fun MarkdownTextField(
  modifier: Modifier = Modifier,
  value: MarkdownTextFieldValue =
    rememberMarkdownTextFieldValue(
      Markdown.styled("John Appleseed", listOf(Style.Bold(0..3), Style.Italic(5..13)))
    )
) {
  MarkdownTextField(
    rememberMarkdownTextFieldState(),
    value,
    onValueChange = {},
    Colors.LIGHT,
    modifier,
    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") }
  ) {
    Text("Name")
  }
}

/**
 * Text field to which stylization can be applied from the provided text.
 *
 * @param state [MarkdownTextFieldState] from which [Style]s can be toggled.
 * @param value [MarkdownTextFieldValue] containing [Markdown] to be displayed.
 * @param onValueChange Lambda that gets invoked whenever the text changes.
 * @param themeColors αὐτός [Colors] by which the [Editable] into which the text is converted will
 *   be colored.
 * @param modifier [Modifier] that is applied to the underlying [AndroidView].
 * @param textFieldColors [TextFieldColors] by which this [MarkdownTextField] is colored.
 * @param padding [PaddingValues] for spacing the surroundings.
 * @param keyboardOptions Software-IME-specific options.
 * @param keyboardActions Software-IME-specific actions.
 * @param leadingIcon Icon displayed before the text.
 * @param placeholder Content shown when the text is empty.
 */
@Composable
fun MarkdownTextField(
  state: MarkdownTextFieldState,
  value: MarkdownTextFieldValue,
  onValueChange: (value: MarkdownTextFieldValue) -> Unit,
  themeColors: Colors,
  modifier: Modifier = Modifier,
  textFieldColors: TextFieldColors = TextFieldDefaults.colors(),
  padding: PaddingValues = PaddingValues(0.dp),
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  leadingIcon: @Composable () -> Unit = {},
  placeholder: @Composable () -> Unit
) {
  val context = LocalContext.current
  val density = LocalDensity.current
  val layoutDirection = LocalLayoutDirection.current
  val focusRequester = remember(::FocusRequester)
  val view = remember(context) { InteropEditText(context) }
  val leadingIconDrawable by drawableStateOf(leadingIcon)
  val leadingIconDrawableWidth =
    remember(density, leadingIconDrawable) {
      with(density) { leadingIconDrawable?.intrinsicWidth?.toDp() }
    }
  val leadingIconSpacing =
    remember(padding, layoutDirection) { padding.calculateStartPadding(layoutDirection) }
  var styles by remember(state) { mutableStateOf(emptyList<Style>()) }
  val updateText: (CharSequence) -> Markdown by rememberUpdatedState {
    Markdown.styled("$it", styles)
  }
  val updateValue: (CharSequence) -> MarkdownTextFieldValue by rememberUpdatedState {
    MarkdownTextFieldValue(updateText(it), view.selectionStart..view.selectionEnd)
  }
  val updatedText = remember(value, styles) { updateText(value.text) }
  val updatedTextAsEditable by updatedText.toAnnotatedString(themeColors).toEditableAsState(context)
  val textSourceOfTruth =
    rememberSourceOfTruth(
      value.text,
      onTextChange = { onValueChange(updateValue(it)) },
      updateText,
      view,
      themeColors
    )
  val onStylizationListener =
    remember(state) {
      MarkdownTextFieldState.OnStylizationListener {
        styles = it
        onValueChange(updateValue(updatedText))
      }
    }

  Box(Modifier.padding(padding), Alignment.CenterStart) {
    AndroidView(
      { view },
      modifier.focusRequester(focusRequester).proxy(view).testTag(MarkdownTextFieldTag),
      onRelease = {
        textSourceOfTruth.detach()
        state.reset()
        it.text?.clearSpans()
        it.text?.clear()
        it.setKeyboardOptions(null)
        it.setKeyboardActions(null)
        it.setCompoundDrawables(null, null, null, null)
        it.setColors(null)
      }
    ) {
      it.setColors(textFieldColors)
      it.setCompoundDrawablesRelativeWithIntrinsicBounds(leadingIconDrawable, null, null, null)
      it.compoundDrawablePadding = with(density) { leadingIconSpacing.roundToPx() }
      it.gravity = Gravity.CENTER_VERTICAL
      it.setKeyboardActions(keyboardActions)
      it.setKeyboardOptions(keyboardOptions)
      it.text = updatedTextAsEditable
      state.setInitialStyles(value.text.styles)
      state.setOnStylizationListener(onStylizationListener)
      state.span(it)
      textSourceOfTruth.attach()
    }

    updatedText.ifEmpty {
      Box(Modifier.padding(start = leadingIconDrawableWidth?.plus(leadingIconSpacing) ?: 0.dp)) {
        placeholder()
      }
    }
  }
}

/** Preview of an empty [MarkdownTextField]. */
@Composable
@Preview
private fun EmptyMarkdownTextFieldPreview() {
  MarkdownTextField(value = MarkdownTextFieldValue.Empty)
}

/** Preview of a populated [MarkdownTextField]. */
@Composable
@Preview
private fun PopulatedMarkdownTextFieldPreview() {
  MarkdownTextField()
}
