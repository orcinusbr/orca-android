/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.platform.autos.kit.input.text

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.autos.forms.Form
import br.com.orcinus.orca.platform.autos.R
import br.com.orcinus.orca.platform.autos.borders.asBorderStroke
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.kit.input.text.FormTextFieldDefaults as _TextFieldDefaults
import br.com.orcinus.orca.platform.autos.kit.input.text.error.ErrorDispatcher
import br.com.orcinus.orca.platform.autos.kit.input.text.error.containsErrorsAsState
import br.com.orcinus.orca.platform.autos.kit.input.text.error.messages
import br.com.orcinus.orca.platform.autos.kit.input.text.error.rememberErrorDispatcher
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/**
 * Tag that identifies a [FormTextField] for testing purposes.
 *
 * @see FormTextField
 */
const val FormTextFieldTag = "form-text-field"

/** Tag that identifies a [FormTextField]'s errors' [Text] for testing purposes. */
const val FormTextFieldErrorsTag = "form-text-field-errors"

/** Default values used by a [FormTextField]. */
object FormTextFieldDefaults {
  /**
   * [TextFieldColors] by which a [FormTextField] is colored by default.
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
        .testTag(FormTextFieldTag),
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
        Modifier.padding(start = errorMessagesStartSpacing).testTag(FormTextFieldErrorsTag),
        errorMessagesColor
      )
    }
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
