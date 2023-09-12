package com.jeanbarrossilva.orca.platform.theme.kit.action

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

/**
 * [ElevatedButton] that represents a primary action, performed or requested to be performed through
 * [onClick]; usually is placed on the bottom of the screen, filling its width.
 *
 * @param onClick Callback called whenever it's clicked.
 * @param modifier [Modifier] to be applied to the underlying [ElevatedButton].
 * @param isEnabled Whether it can be interacted with.
 * @param content Content to be placed inside of it; generally a [Text] that shortly explains the
 * action performed by [onClick].
 **/
@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val enabledContentColor = OrcaTheme.colors.primary.content
    val disabledContentColor = OrcaTheme.colors.disabled.content
    val contentColor =
        remember(isEnabled) { if (isEnabled) enabledContentColor else disabledContentColor }
    var isLoading by remember { mutableStateOf(false) }

    ElevatedButton(
        onClick = {
            isLoading = true
            onClick()
        },
        modifier,
        isEnabled,
        shape = OrcaTheme.shapes.medium,
        colors = ButtonDefaults.elevatedButtonColors(
            OrcaTheme.colors.primary.container,
            enabledContentColor,
            OrcaTheme.colors.disabled.container,
            disabledContentColor
        ),
        contentPadding = PaddingValues(OrcaTheme.spacings.large)
    ) {
        ProvideTextStyle(LocalTextStyle.current.copy(color = contentColor)) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(17.4.dp), strokeCap = StrokeCap.Round)
            } else {
                content()
            }
        }
    }
}

@Composable
@MultiThemePreview
private fun DisabledPrimaryButtonPreview() {
    OrcaTheme {
        PrimaryButton(isEnabled = false)
    }
}

/** Preview of an enabled [PrimaryButton]. **/
@Composable
@MultiThemePreview
private fun EnabledPrimaryButtonPreview() {
    OrcaTheme {
        PrimaryButton(isEnabled = true)
    }
}

/**
 * [ElevatedButton] that represents a primary action; usually is placed on the bottom of the screen,
 * filling its width.
 *
 * @param isEnabled Whether it can be interacted with.
 * @param modifier [Modifier] to be applied to the underlying [ElevatedButton].
 **/
@Composable
private fun PrimaryButton(isEnabled: Boolean, modifier: Modifier = Modifier) {
    PrimaryButton(onClick = { }, modifier, isEnabled) {
        Text("Label")
    }
}
