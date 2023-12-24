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

import androidx.annotation.RestrictTo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.autos.forms.Form
import com.jeanbarrossilva.orca.platform.autos.R
import com.jeanbarrossilva.orca.platform.autos.borders.asBorderStroke
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.TextField as _TextField
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.TextFieldDefaults as _TextFieldDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.ErrorDispatcher
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.buildErrorDispatcher
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.containsErrorsAsState
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.messages
import com.jeanbarrossilva.orca.platform.autos.kit.input.text.error.rememberErrorDispatcher
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [TextField]'s errors' [Text] for testing purposes. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
const val TEXT_FIELD_ERRORS_TAG = "text-field-errors"

/** Default values used by a [TextField][_TextField]. */
object TextFieldDefaults {
  /**
   * [TextFieldColors] by which a [TextField][_TextField] is colored by default.
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
 * Orca-specific [TextField].
 *
 * @param text Text to be shown.
 * @param onTextChange Callback called whenever the text changes.
 * @param modifier [Modifier] to be applied to the underlying [TextField].
 * @param errorDispatcher [ErrorDispatcher] by which invalid input state errors will be dispatched.
 * @param keyboardOptions Software-IME-specific options.
 * @param keyboardActions Software-IME-specific actions.
 * @param isSingleLined Whether there can be multiple lines.
 */
@Composable
fun TextField(
  text: String,
  onTextChange: (text: String) -> Unit,
  modifier: Modifier = Modifier,
  errorDispatcher: ErrorDispatcher = rememberErrorDispatcher(),
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  isSingleLined: Boolean = false,
  label: @Composable () -> Unit
) {
  val context = LocalContext.current
  val form = AutosTheme.forms.large as Form.PerCorner
  val shape = remember(form, form::asShape)
  val errorMessages =
    errorDispatcher.messages.joinToString("\n") {
      context.getString(R.string.platform_ui_text_field_consecutive_error_message, it)
    }
  val containsErrors by errorDispatcher.containsErrorsAsState

  DisposableEffect(text) {
    errorDispatcher.register(text)
    onDispose {}
  }

  Column(modifier, Arrangement.spacedBy(AutosTheme.spacings.medium.dp)) {
    BoxWithConstraints {
      TextField(
        text,
        onTextChange,
        Modifier.border(
            AutosTheme.borders.default.asBorderStroke.width,
            AutosTheme.borders.default.asBorderStroke.brush,
            shape
          )
          .width(maxWidth),
        label = {
          val color =
            if (containsErrors) {
              AutosTheme.colors.error.container.asColor
            } else {
              LocalContentColor.current
            }
          val style = LocalTextStyle.current.copy(color = color)
          ProvideTextStyle(style) { label() }
        },
        isError = containsErrors,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = isSingleLined,
        shape = shape,
        colors = _TextFieldDefaults.colors()
      )
    }

    AnimatedVisibility(visible = containsErrors) {
      Text(
        errorMessages,
        Modifier.padding(start = form.bottomStart.dp).testTag(TEXT_FIELD_ERRORS_TAG),
        AutosTheme.colors.error.container.asColor
      )
    }
  }
}

/**
 * Orca-specific [TextField].
 *
 * @param modifier [Modifier] to be applied to the underlying [TextField].
 * @param text Text to be shown.
 * @param errorDispatcher [ErrorDispatcher] to which invalid input state errors will be dispatched.
 */
@Composable
internal fun TextField(
  modifier: Modifier = Modifier,
  text: String = "Text",
  errorDispatcher: ErrorDispatcher = rememberErrorDispatcher()
) {
  _TextField(text, onTextChange = {}, modifier, errorDispatcher) { Text("Label") }
}

/** Preview of a focused [TextField][_TextField]. */
@Composable
@MultiThemePreview
private fun ValidTextFieldPreview() {
  AutosTheme { _TextField() }
}

/** Preview of a [TextField] with errors. */
@Composable
@MultiThemePreview
private fun InvalidTextFieldPreview() {
  AutosTheme {
    _TextField(
      errorDispatcher =
        buildErrorDispatcher {
            errorAlways("This is an error.")
            errorAlways("This is another error. 😛")
          }
          .apply(ErrorDispatcher::dispatch)
    )
  }
}
