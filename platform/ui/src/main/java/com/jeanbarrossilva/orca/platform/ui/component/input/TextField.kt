package com.jeanbarrossilva.orca.platform.ui.component.input

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

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

    TextField(
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
    val borderWidth by animateDpAsState(if (isFocused) 2.dp else 0.dp)
    val highlightColor = OrcaTheme.colorScheme.primary
    val borderColor by animateColorAsState(if (isFocused) highlightColor else Color.Transparent)
    val shape = OrcaTheme.shapes.medium

    TextField(
        text,
        onTextChange,
        modifier.border(borderWidth, borderColor, shape),
        label = {
            val color by animateColorAsState(
                if (isFocused) highlightColor else LocalTextStyle.current.color
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
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        )
    )
}

/**
 * Preview of an empty
 * [TextField][com.jeanbarrossilva.orca.platform.ui.component.input.TextField].
 **/
@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun EmptyTextFieldPreview() {
    OrcaTheme {
        TextField(text = "")
    }
}

/**
 * Preview of an unfocused
 * [TextField][com.jeanbarrossilva.orca.platform.ui.component.input.TextField].
 **/
@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun UnfocusedTextFieldPreview() {
    OrcaTheme {
        TextField(isFocused = false)
    }
}

/**
 * Preview of a focused
 * [TextField][com.jeanbarrossilva.orca.platform.ui.component.input.TextField].
 **/
@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun FocusedTextFieldPreview() {
    OrcaTheme {
        TextField(isFocused = true)
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
    TextField(text, onTextChange = { }, isFocused, modifier) {
        Text("Label")
    }
}
