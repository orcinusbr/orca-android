package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconDefaults

internal object StatDefaults {
    val IconSize = ActivateableStatIconDefaults.Size

    val containerColor
        @Composable get() = OrcaTheme.colors.surface.container
}

@Composable
internal fun Stat(
    vector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = StatDefaults.containerColor,
    label: @Composable () -> Unit = { }
) {
    Stat(onClick, modifier, containerColor) {
        Icon(
            vector,
            contentDescription,
            Modifier
                .clickable(onClick = onClick)
                .size(StatDefaults.IconSize)
        )

        label()
    }
}

@Composable
internal fun Stat(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = StatDefaults.containerColor,
    content: @Composable RowScope.() -> Unit
) {
    val shape = CardDefaults.shape
    val spacing = OrcaTheme.spacings.small
    val contentColor = if (containerColor == StatDefaults.containerColor) {
        OrcaTheme.colors.secondary
    } else {
        fallbackContentColorFor(containerColor)
    }

    Row(
        modifier
            .shadow(4.dp, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .clip(shape)
            .background(containerColor)
            .padding(spacing),
        Arrangement.spacedBy(spacing),
        Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides OrcaTheme.typography.bodySmall.copy(color = contentColor)
        ) {
            content()
        }
    }
}

private fun fallbackContentColorFor(containerColor: Color): Color {
    return if (containerColor.luminance() < .5f) Color.White else Color.Black
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
internal fun StatPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background) {
            Stat(
                OrcaTheme.iconography.comment.outlined,
                contentDescription = "Comment",
                onClick = { }
            ) {
                Text("8")
            }
        }
    }
}
