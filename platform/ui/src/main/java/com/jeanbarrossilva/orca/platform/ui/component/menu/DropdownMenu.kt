package com.jeanbarrossilva.orca.platform.ui.component.menu

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

/**
 * [Popup] menu that displays a variety of options through [DropdownMenuItem]s in its [content].
 *
 * An Orca-specific version of [androidx.compose.material3.DropdownMenu].
 *
 * @param isExpanded Whether it's being shown.
 * @param onDismissal Callback run when it is requested to be dismissed.
 * @param modifier [Modifier] to be applied to the underlying [DropdownMenu].
 * @param content [DropdownMenuItem]s contained by this
 * [DropdownMenu][com.jeanbarrossilva.orca.platform.ui.component.menu.DropdownMenu].
 **/
@Composable
fun DropdownMenu(
    isExpanded: Boolean,
    onDismissal: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    DropdownMenu(
        isExpanded,
        onDismissal,
        modifier.background(OrcaTheme.colors.surface.container),
        content = content
    )
}

/**
 * Preview of a [DropdownMenu][com.jeanbarrossilva.orca.platform.ui.component.menu.DropdownMenu].
 **/
@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun DropdownMenuPreview() {
    OrcaTheme {
        DropdownMenu(isExpanded = true, onDismissal = { }) {
            repeat(8) {
                DropdownMenuItem(text = { Text("Item $it") }, onClick = { })
            }
        }
    }
}
