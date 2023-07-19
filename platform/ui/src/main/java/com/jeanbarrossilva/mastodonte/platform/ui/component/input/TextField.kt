package com.jeanbarrossilva.mastodonte.platform.ui.component.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

/**
 * Mastodonte-specific [TextField].
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
 * Mastodonte-specific [TextField].
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
    val borderColor by animateColorAsState(
        if (isFocused) MastodonteTheme.colorScheme.primary else Color.Transparent
    )
    val shape = MastodonteTheme.shapes.medium

    TextField(
        text,
        onTextChange,
        modifier.border(borderWidth, borderColor, shape),
        label = label,
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
 * [TextField][com.jeanbarrossilva.mastodonte.platform.ui.component.input.TextField].
 **/
@Composable
@Preview
private fun EmptyTextFieldPreview() {
    MastodonteTheme {
        TextField(text = "")
    }
}

/**
 * Preview of an unfocused
 * [TextField][com.jeanbarrossilva.mastodonte.platform.ui.component.input.TextField].
 **/
@Composable
@Preview
private fun UnfocusedTextFieldPreview() {
    MastodonteTheme {
        TextField(isFocused = false)
    }
}

/**
 * Preview of a focused
 * [TextField][com.jeanbarrossilva.mastodonte.platform.ui.component.input.TextField].
 **/
@Composable
@Preview
private fun FocusedTextFieldPreview() {
    MastodonteTheme {
        TextField(isFocused = true)
    }
}

/**
 * Mastodonte-specific [TextField].
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
