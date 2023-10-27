package com.jeanbarrossilva.orca.platform.theme.kit.input.text

import androidx.annotation.RestrictTo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.R
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.TextField as _TextField
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.TextFieldDefaults as _TextFieldDefaults
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.error.ErrorDispatcher
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.error.buildErrorDispatcher
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.error.containsErrorsAsState
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.error.messages
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.error.rememberErrorDispatcher

/** Tag that identifies a [TextField]'s errors' [Text] for testing purposes. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
const val TEXT_FIELD_ERRORS_TAG = "text-field-errors"

/** Default values used by a [TextField][_TextField]. */
object TextFieldDefaults {
  /**
   * [TextFieldColors] by which a [TextField][_TextField] is colored by default.
   *
   * @param enabledContainerColor [Color] to color the container with when the
   *   [TextField][_TextField] is enabled.
   */
  @Composable
  fun colors(enabledContainerColor: Color = OrcaTheme.colors.surface.container): TextFieldColors {
    return TextFieldDefaults.colors(
      focusedContainerColor = enabledContainerColor,
      unfocusedContainerColor = enabledContainerColor,
      cursorColor = contentColorFor(enabledContainerColor),
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
 * @param errorDispatcher [ErrorDispatcher] to which invalid input state errors will be dispatched.
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
  var isFocused by remember { mutableStateOf(false) }

  _TextField(
    text,
    onTextChange,
    isFocused,
    modifier.onFocusChanged { isFocused = it.isFocused },
    errorDispatcher,
    keyboardOptions,
    keyboardActions,
    isSingleLined,
    label
  )
}

/**
 * Orca-specific [TextField].
 *
 * @param modifier [Modifier] to be applied to the underlying [TextField].
 * @param text Text to be shown.
 * @param isFocused Whether it's focused.
 * @param errorDispatcher [ErrorDispatcher] to which invalid input state errors will be dispatched.
 */
@Composable
internal fun TextField(
  modifier: Modifier = Modifier,
  text: String = "Text",
  isFocused: Boolean = false,
  errorDispatcher: ErrorDispatcher = rememberErrorDispatcher()
) {
  _TextField(text, onTextChange = {}, isFocused, modifier, errorDispatcher) { Text("Label") }
}

/**
 * Orca-specific [TextField].
 *
 * @param text Text to be shown.
 * @param onTextChange Callback called whenever the text changes.
 * @param isFocused Whether it's focused.
 * @param modifier [Modifier] to be applied to the underlying [TextField].
 * @param errorDispatcher [ErrorDispatcher] by which invalid input state errors will be dispatched.
 * @param keyboardOptions Software-IME-specific options.
 * @param keyboardActions Software-IME-specific actions.
 * @param isSingleLined Whether there can be multiple lines.
 */
@Composable
private fun TextField(
  text: String,
  onTextChange: (text: String) -> Unit,
  isFocused: Boolean,
  modifier: Modifier = Modifier,
  errorDispatcher: ErrorDispatcher = rememberErrorDispatcher(),
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  isSingleLined: Boolean = false,
  label: @Composable () -> Unit
) {
  val context = LocalContext.current
  val highlightColor = OrcaTheme.colors.secondary
  val borderColor by
    animateColorAsState(
      if (isFocused) highlightColor else (OrcaTheme.borders.default.brush as SolidColor).value,
      label = "BorderColor"
    )
  val shape = OrcaTheme.shapes.large
  val errorMessages =
    errorDispatcher.messages.joinToString("\n") {
      context.getString(R.string.platform_ui_text_field_consecutive_error_message, it)
    }
  val containsErrors by errorDispatcher.containsErrorsAsState

  DisposableEffect(text) {
    errorDispatcher.register(text)
    onDispose {}
  }

  Column(verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.medium)) {
    TextField(
      text,
      onTextChange,
      modifier.border(OrcaTheme.borders.default.width, borderColor, shape),
      label = {
        val color by
          animateColorAsState(
            if (isFocused) highlightColor else LocalTextStyle.current.color,
            label = "LabelColor"
          )

        ProvideTextStyle(LocalTextStyle.current.copy(color = color)) { label() }
      },
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      singleLine = isSingleLined,
      shape = shape,
      colors = _TextFieldDefaults.colors()
    )

    AnimatedVisibility(visible = containsErrors) {
      Text(errorMessages, Modifier.testTag(TEXT_FIELD_ERRORS_TAG), OrcaTheme.colors.error.container)
    }
  }
}

/** Preview of an empty [TextField][_TextField]. */
@Composable
@MultiThemePreview
private fun EmptyTextFieldPreview() {
  OrcaTheme { _TextField(text = "") }
}

/** Preview of an unfocused [TextField][_TextField]. */
@Composable
@MultiThemePreview
private fun UnfocusedTextFieldPreview() {
  OrcaTheme { _TextField(isFocused = false) }
}

/** Preview of a focused [TextField][_TextField]. */
@Composable
@MultiThemePreview
private fun FocusedTextFieldPreview() {
  OrcaTheme { _TextField(isFocused = true) }
}

/** Preview of a [TextField] with errors. */
@Composable
@MultiThemePreview
private fun InvalidTextFieldPreview() {
  OrcaTheme {
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
