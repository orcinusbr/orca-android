package com.jeanbarrossilva.mastodonte.platform.ui.component.action

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

/**
 * [ElevatedButton] that represents a primary action, performed or requested to be performed through
 * [onClick]; usually is placed on the bottom of the screen, filling its width.
 *
 * @param onClick Callback called whenever it's clicked.
 * @param modifier [Modifier] to be applied to the underlying [ElevatedButton].
 * @param content Content to be placed inside of it; generally a [Text] that shortly explains the
 * action performed by [onClick].
 **/
@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ElevatedButton(
        onClick,
        modifier,
        shape = MastodonteTheme.shapes.medium,
        colors = ButtonDefaults
            .elevatedButtonColors(containerColor = MastodonteTheme.colorScheme.primary),
        contentPadding = PaddingValues(MastodonteTheme.spacings.large)
    ) {
        content()
    }
}

/** Preview of a [PrimaryButton]. **/
@Composable
@Preview
private fun PrimaryButtonPreview() {
    MastodonteTheme {
        PrimaryButton(onClick = { }) {
            Text("Label")
        }
    }
}
