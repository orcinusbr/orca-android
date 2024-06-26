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

import android.view.Gravity
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.setPadding
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.annotated.toAnnotatedString
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.annotated.toEditableAsState
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop.InteropEditText
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop.drawableStateOf
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop.proxy
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.interop.truth.text.rememberSourceOfTruth
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.state.MarkdownTextFieldState
import br.com.orcinus.orca.platform.autos.kit.input.text.markdown.state.rememberMarkdownTextFieldState
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style

/** Tag that identifies a [MarkdownTextField] for testing purposes. */
internal const val MarkdownTextFieldTag = "markdown-text-field"

/** Default values used by a [MarkdownTextField]. */
internal object MarkdownTextFieldDefaults {
  /** Amount of [Dp]s that spaces the surroundings by default. */
  val spacing
    @Composable get() = AutosTheme.spacings.medium.dp
}

/**
 * [Markdown]-based text field with a no-op change listener.
 *
 * @param modifier [Modifier] that is applied to the underlying [AndroidView].
 * @param value [MarkdownTextFieldValue] containing [Markdown] to be displayed.
 */
@Composable
@VisibleForTesting
internal fun MarkdownTextField(
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
    modifier,
    leadingIcon = {
      Icon(AutosTheme.iconography.profile.filled.asImageVector, contentDescription = "Name")
    }
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
 * @param modifier [Modifier] that is applied to the underlying [AndroidView].
 * @param keyboardOptions Software-IME-specific options.
 * @param keyboardActions Software-IME-specific actions.
 * @param leadingIcon Icon displayed before the text.
 * @param placeholder Content shown when the text is empty.
 */
@Composable
internal fun MarkdownTextField(
  state: MarkdownTextFieldState,
  value: MarkdownTextFieldValue,
  onValueChange: (value: MarkdownTextFieldValue) -> Unit,
  modifier: Modifier = Modifier,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  leadingIcon: @Composable () -> Unit = {},
  placeholder: @Composable () -> Unit = {}
) {
  val context = LocalContext.current
  val density = LocalDensity.current
  val focusRequester = remember(::FocusRequester)
  val view = remember(context) { InteropEditText(context) }
  val containerColor = AutosTheme.colors.background.container.asColor
  val colors =
    TextFieldDefaults.colors(
      focusedContainerColor = containerColor,
      unfocusedContainerColor = containerColor
    )
  val spacing = MarkdownTextFieldDefaults.spacing
  val leadingIconDrawable by drawableStateOf(leadingIcon)
  val leadingIconDrawableWidth =
    remember(density, leadingIconDrawable) {
      with(density) { leadingIconDrawable?.intrinsicWidth?.toDp() }
    }
  var styles by remember(state) { mutableStateOf(emptyList<Style>()) }
  val updateText: (CharSequence) -> Markdown by rememberUpdatedState {
    Markdown.styled("$it", styles)
  }
  val updateValue: (CharSequence) -> MarkdownTextFieldValue by rememberUpdatedState {
    MarkdownTextFieldValue(updateText(it), view.selectionStart..view.selectionEnd)
  }
  val updatedText = remember(value, styles) { updateText(value.text) }
  val updatedTextAsEditable by updatedText.toAnnotatedString().toEditableAsState(context)
  val textSourceOfTruth =
    rememberSourceOfTruth(
      value.text,
      onTextChange = { onValueChange(updateValue(it)) },
      updateText,
      view
    )
  val onStylizationListener =
    remember(state) {
      MarkdownTextFieldState.OnStylizationListener {
        styles = it
        onValueChange(updateValue(updatedText))
      }
    }

  Box(Modifier.background(containerColor), Alignment.CenterStart) {
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
      it.setColors(colors)
      it.setCompoundDrawablesRelativeWithIntrinsicBounds(leadingIconDrawable, null, null, null)
      it.compoundDrawablePadding = with(density) { spacing.roundToPx() }
      it.gravity = Gravity.CENTER_VERTICAL
      it.setPadding(with(density) { spacing.roundToPx() })
      it.setKeyboardActions(keyboardActions)
      it.setKeyboardOptions(keyboardOptions)
      it.text = updatedTextAsEditable
      state.setInitialStyles(value.text.styles)
      state.setOnStylizationListener(onStylizationListener)
      state.span(it)
      textSourceOfTruth.attach()
    }

    updatedText.ifEmpty {
      Box(
        Modifier.padding(start = leadingIconDrawableWidth?.plus(spacing) ?: 0.dp).padding(spacing)
      ) {
        ProvideTextStyle(
          LocalTextStyle.current.copy(color = AutosTheme.colors.tertiary.asColor),
          placeholder
        )
      }
    }
  }
}

/** Preview of an empty [MarkdownTextField]. */
@Composable
@Preview
private fun EmptyMarkdownTextFieldPreview() {
  AutosTheme { MarkdownTextField(value = MarkdownTextFieldValue.Empty) }
}

/** Preview of a populated [MarkdownTextField]. */
@Composable
@Preview
private fun PopulatedMarkdownTextFieldPreview() {
  AutosTheme { MarkdownTextField() }
}
