package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

internal object StatDefaults {
    val IconSize = 18.dp

    val containerColor
        @Composable get() = MastodonteTheme.colorScheme.surfaceVariant
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
    val contentColor = if (containerColor == StatDefaults.containerColor) {
        MastodonteTheme.colorScheme.outline
    } else {
        contentColorFor(containerColor)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    Card(
        onClick,
        modifier.semantics { role = Role.Button },
        colors = CardDefaults.cardColors(containerColor),
        border = BorderStroke(2.dp, Color.Black.copy(alpha = .2f).compositeOver(containerColor))
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides MastodonteTheme.typography.bodySmall.copy(color = contentColor)
        ) {
            Row(
                Modifier.padding(MastodonteTheme.spacings.small),
                Arrangement.spacedBy(MastodonteTheme.spacings.small),
                Alignment.CenterVertically,
                content
            )
        }
    }
}

@Composable
@Preview
internal fun StatPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Stat(MastodonteTheme.Icons.ThumbUp, contentDescription = "Like", onClick = { }) {
                Text("8")
            }
        }
    }
}
