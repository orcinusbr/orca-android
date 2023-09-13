package com.jeanbarrossilva.orca.platform.theme.kit.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.input.TextField as _TextField
import com.jeanbarrossilva.orca.platform.theme.kit.input.TextFieldDefaults as _TextFieldDefaults

/** Default values used by a [TextField][_TextField]. **/
object TextFieldDefaults {
    /**
     * [TextFieldColors] by which a [TextField][_TextField] is colored by default.
     *
     * @param enabledContainerColor [Color] to color the container with when the
     * [TextField][_TextField] is enabled.
     **/
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
 * @param keyboardOptions Software-IME-specific options.
 * @param keyboardActions Software-IME-specific actions.
 * @param isSingleLined Whether there can be multiple lines.
 **/
@Composable
fun TextField(
    text: String,
    onTextChange: (text: String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isSingleLined: Boolean = false,
    label: @Composable () -> Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    _TextField(
        text,
        onTextChange,
        isFocused,
        modifier.onFocusChanged { isFocused = it.isFocused },
        keyboardOptions,
        keyboardActions,
        isSingleLined,
        label
    )
}

/**
 * Orca-specific [TextField].
 *
 * @param text Text to be shown.
 * @param onTextChange Callback called whenever the text changes.
 * @param isFocused Whether it's focused.
 * @param modifier [Modifier] to be applied to the underlying [TextField].
 * @param keyboardOptions Software-IME-specific options.
 * @param keyboardActions Software-IME-specific actions.
 * @param isSingleLined Whether there can be multiple lines.
 **/
@Composable
private fun TextField(
    text: String,
    onTextChange: (text: String) -> Unit,
    isFocused: Boolean,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isSingleLined: Boolean = false,
    label: @Composable () -> Unit
) {
    val borderWidth by animateDpAsState(if (isFocused) 2.dp else 0.dp, label = "BorderWidth")
    val highlightColor = OrcaTheme.colors.secondary
    val borderColor by animateColorAsState(
        if (isFocused) highlightColor else Color.Transparent,
        label = "BorderColor"
    )
    val shape = OrcaTheme.shapes.large

    TextField(
        text,
        onTextChange,
        modifier.border(borderWidth, borderColor, shape),
        label = {
            val color by animateColorAsState(
                if (isFocused) highlightColor else LocalTextStyle.current.color,
                label = "LabelColor"
            )

            ProvideTextStyle(
                LocalTextStyle.current.copy(color = color)
            ) {
                label()
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = isSingleLined,
        shape = shape,
        colors = _TextFieldDefaults.colors()
    )
}

/** Preview of an empty [TextField][_TextField]. **/
@Composable
@MultiThemePreview
private fun EmptyTextFieldPreview() {
    OrcaTheme {
        _TextField(text = "")
    }
}

/** Preview of an unfocused [TextField][_TextField]. **/
@Composable
@MultiThemePreview
private fun UnfocusedTextFieldPreview() {
    OrcaTheme {
        _TextField(isFocused = false)
    }
}

/** Preview of a focused [TextField][_TextField]. **/
@Composable
@MultiThemePreview
private fun FocusedTextFieldPreview() {
    OrcaTheme {
        _TextField(isFocused = true)
    }
}

/**
 * Orca-specific [TextField].
 *
 * @param text Text to be shown.
 * @param isFocused Whether it's focused.
 * @param modifier [Modifier] to be applied to the underlying [TextField].
 **/
@Composable
private fun TextField(
    modifier: Modifier = Modifier,
    text: String = "Text",
    isFocused: Boolean = false
) {
    _TextField(text, onTextChange = { }, isFocused, modifier) {
        Text("Label")
    }
}
