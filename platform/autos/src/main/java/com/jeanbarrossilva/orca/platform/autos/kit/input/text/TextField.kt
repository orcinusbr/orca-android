/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.input.text

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.autos.forms.Form
import com.jeanbarrossilva.loadable.placeholder.Placeholder
import com.jeanbarrossilva.orca.platform.autos.R
import com.jeanbarrossilva.orca.platform.autos.borders.asBorderStroke
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.TextFieldDefaults as _TextFieldDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.ErrorDispatcher
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.containsErrorsAsState
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.messages
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.rememberErrorDispatcher
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * Tag that identifies a text field for testing purposes.
 *
 * @see CompositionTextField
 * @see FormTextField
 */
const val TEXT_FIELD_TAG = "text-field"

/** Tag that identifies a text field's errors' [Text] for testing purposes. */
const val TEXT_FIELD_ERRORS_TAG = "text-field-errors"

/** Default values used by Orca's text fields. */
object TextFieldDefaults {
  /** Amount of [Dp]s by which a [CompositionTextField] is spaced by default. */
  val compositionSpacing
    @Composable get() = AutosTheme.spacings.medium.dp

  /**
   * [TextFieldColors] by which a text field is colored by default.
   *
   * @param containerColor [Color] to color the container with.
   */
  @Composable
  fun colors(containerColor: Color = AutosTheme.colors.surface.container.asColor): TextFieldColors {
    return TextFieldDefaults.colors(
      focusedContainerColor = containerColor,
      unfocusedContainerColor = containerColor,
      errorContainerColor = containerColor,
      cursorColor = contentColorFor(containerColor),
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent,
      disabledIndicatorColor = Color.Transparent,
      errorIndicatorColor = Color.Transparent
    )
  }
}

/**
 * Orca-specific text field for composition.
 *
 * @param modifier [Modifier] to be applied to the underlying [ContentWithErrors].
 * @param errorDispatcher [ErrorDispatcher] by which invalid input state errors will be dispatched.
 */
@Composable
@VisibleForTesting
fun CompositionTextField(
  modifier: Modifier = Modifier,
  errorDispatcher: ErrorDispatcher = rememberErrorDispatcher()
) {
  CompositionTextField(
    remember { TextFieldValue("") },
    onValueChange = {},
    leadingIcon = { Placeholder(Modifier.size(42.dp), shape = AutosTheme.forms.small.asShape) },
    onSend = {},
    modifier,
    errorDispatcher
  ) {
    Text("Placeholder")
  }
}

/**
 * Orca-specific [TextField] for forms.
 *
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param errorDispatcher [ErrorDispatcher] to which invalid input state errors will be dispatched.
 */
@Composable
@VisibleForTesting
fun FormTextField(
  modifier: Modifier = Modifier,
  errorDispatcher: ErrorDispatcher = rememberErrorDispatcher()
) {
  FormTextField(modifier, text = "", errorDispatcher)
}

/**
 * Orca-specific text field for composition.
 *
 * @param value [TextFieldValue] with the text to be shown.
 * @param onValueChange Callback called whenever the [TextFieldValue] changes.
 * @param leadingIcon Icon displayed before the [value].
 * @param onSend Action performed when the [value] is requested to be sent.
 * @param modifier [Modifier] to be applied to the underlying [ContentWithErrors].
 * @param errorDispatcher [ErrorDispatcher] by which invalid input state errors will be dispatched.
 * @param colors [TextFieldColors] by which it its colored.
 * @param placeholder [Text] to be shown when the [CompositionTextField] is focused and the [value]
 *   is empty.
 */
@Composable
fun CompositionTextField(
  value: TextFieldValue,
  onValueChange: (value: TextFieldValue) -> Unit,
  leadingIcon: @Composable () -> Unit,
  onSend: () -> Unit,
  modifier: Modifier = Modifier,
  errorDispatcher: ErrorDispatcher = rememberErrorDispatcher(),
  colors: TextFieldColors = _TextFieldDefaults.colors(),
  placeholder: @Composable () -> Unit = {}
) {
  val interactionSource = remember(::MutableInteractionSource)

  // TODO: Internalize Form subclasses' constructors in αὐτός.
  // TODO: Add Form.Companion.Rectangular to αὐτός.
  val shape = remember {
    Form.PerCorner(topStart = 0f, topEnd = 0f, bottomStart = 0f, bottomEnd = 0f).asShape
  }

  val style = LocalTextStyle.current
  var containerColor by remember(colors) { mutableStateOf(Color.Unspecified) }
  val cursorBrushColor = AutosTheme.colors.primary.container.asColor
  val cursorBrush = remember(cursorBrushColor) { SolidColor(cursorBrushColor) }
  val spacing = _TextFieldDefaults.compositionSpacing

  ContentWithErrors(
    value.text,
    errorDispatcher,
    errorMessagesStartSpacing = spacing,
    modifier.background(containerColor).padding(bottom = spacing)
  ) { containsErrors, secondaryTextStyle ->
    containerColor =
      colors.containerColor(isEnabled = true, containsErrors, interactionSource).value

    BasicTextField(
      value,
      onValueChange,
      Modifier.clip(shape).padding(spacing).fillMaxWidth().testTag(TEXT_FIELD_TAG),
      textStyle = style,
      keyboardActions = KeyboardActions(onSend = { onSend() }),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
      interactionSource = interactionSource,
      cursorBrush = cursorBrush
    ) { innerTextField ->
      @OptIn(ExperimentalMaterial3Api::class)
      TextFieldDefaults.DecorationBox(
        value.text,
        innerTextField,
        enabled = true,
        singleLine = false,
        VisualTransformation.None,
        interactionSource,
        placeholder = { ProvideTextStyle(secondaryTextStyle) { placeholder() } },
        leadingIcon = leadingIcon,
        colors = colors,
        contentPadding = PaddingValues(start = spacing * 2)
      )
    }
  }
}

/**
 * Orca-specific text field for forms.
 *
 * @param text Text to be shown.
 * @param onTextChange Callback called whenever the text changes.
 * @param modifier [Modifier] to be applied to the underlying [ContentWithErrors].
 * @param errorDispatcher [ErrorDispatcher] by which invalid input state errors will be dispatched.
 * @param keyboardOptions Software-IME-specific options.
 * @param keyboardActions Software-IME-specific actions.
 * @param isSinglyLined Whether there can be only one line.
 */
@Composable
fun FormTextField(
  text: String,
  onTextChange: (text: String) -> Unit,
  modifier: Modifier = Modifier,
  errorDispatcher: ErrorDispatcher = rememberErrorDispatcher(),
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  isSinglyLined: Boolean = false,
  label: @Composable () -> Unit
) {
  val form = AutosTheme.forms.large as Form.PerCorner
  val shape = remember(form, form::asShape)

  ContentWithErrors(
    text,
    errorDispatcher,
    errorMessagesStartSpacing = form.bottomStart.dp,
    modifier
  ) { containsErrors, secondaryTextStyle ->
    TextField(
      text,
      onTextChange,
      Modifier.border(
          AutosTheme.borders.default.asBorderStroke.width,
          AutosTheme.borders.default.asBorderStroke.brush,
          shape
        )
        .width(maxWidth)
        .testTag(TEXT_FIELD_TAG),
      label = { ProvideTextStyle(secondaryTextStyle) { label() } },
      isError = containsErrors,
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      singleLine = isSinglyLined,
      shape = shape,
      colors = _TextFieldDefaults.colors()
    )
  }
}

/**
 * Orca-specific text field for forms.
 *
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param text Text to be shown.
 * @param errorDispatcher [ErrorDispatcher] to which invalid input state errors will be dispatched.
 */
@Composable
internal fun FormTextField(
  modifier: Modifier = Modifier,
  text: String = "Text",
  errorDispatcher: ErrorDispatcher = rememberErrorDispatcher()
) {
  FormTextField(text, onTextChange = {}, modifier, errorDispatcher) { Text("Label") }
}

/**
 * Shows the [content] of a text field with the errors dispatched by the [errorDispatcher].
 *
 * @param text Text to be shown.
 * @param errorDispatcher [ErrorDispatcher] by which invalid input state errors will be dispatched.
 * @param errorMessagesStartSpacing Leading spacing between the error messages and the start of the
 *   [Composable].
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param content Text field to which the errors to be eventually shown are associated.
 */
@Composable
private fun ContentWithErrors(
  text: String,
  errorDispatcher: ErrorDispatcher,
  errorMessagesStartSpacing: Dp,
  modifier: Modifier = Modifier,
  content:
    @Composable
    BoxWithConstraintsScope.(containsErrors: Boolean, secondaryTextStyle: TextStyle) -> Unit
) {
  val context = LocalContext.current
  val errorMessages =
    errorDispatcher.messages.joinToString("\n") {
      context.getString(R.string.platform_ui_text_field_consecutive_error_message, it)
    }
  val containsErrors by errorDispatcher.containsErrorsAsState
  val errorMessagesColor =
    if (containsErrors) {
      AutosTheme.colors.error.container.asColor
    } else {
      LocalContentColor.current
    }

  DisposableEffect(text) {
    errorDispatcher.register(text)
    onDispose {}
  }

  Column(modifier, Arrangement.spacedBy(AutosTheme.spacings.medium.dp)) {
    BoxWithConstraints {
      content(
        containsErrors,
        LocalTextStyle.current.copy(color = AutosTheme.colors.tertiary.asColor)
      )
    }

    AnimatedVisibility(visible = containsErrors) {
      Text(
        errorMessages,
        Modifier.padding(start = errorMessagesStartSpacing).testTag(TEXT_FIELD_ERRORS_TAG),
        errorMessagesColor
      )
    }
  }
}

/** Preview of a valid [CompositionTextField]. */
@Composable
@MultiThemePreview
private fun ValidCompositionTextFieldPreview() {
  AutosTheme { CompositionTextField() }
}

/** Preview of a [CompositionTextField] with errors. */
@Composable
@MultiThemePreview
private fun InvalidCompositionTextFieldPreview() {
  AutosTheme {
    CompositionTextField(
      errorDispatcher =
        rememberErrorDispatcher {
            errorAlways("This is an error.")
            errorAlways("This is another error. 😛")
          }
          .apply(ErrorDispatcher::dispatch)
    )
  }
}

/** Preview of a valid [FormTextField]. */
@Composable
@MultiThemePreview
private fun ValidFormTextFieldPreview() {
  AutosTheme { FormTextField() }
}

/** Preview of a [FormTextField] with errors. */
@Composable
@MultiThemePreview
private fun InvalidFormTextFieldPreview() {
  AutosTheme {
    FormTextField(
      errorDispatcher =
        rememberErrorDispatcher {
            errorAlways("This is an error.")
            errorAlways("This is another error. 😛")
          }
          .apply(ErrorDispatcher::dispatch)
    )
  }
}
