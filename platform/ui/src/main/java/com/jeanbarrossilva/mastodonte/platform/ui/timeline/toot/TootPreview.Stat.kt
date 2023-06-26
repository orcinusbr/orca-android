package com.jeanbarrossilva.mastodonte.platform.ui.timeline.toot

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

@Composable
internal fun Stat(
    vector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit = { }
) {
    @OptIn(ExperimentalMaterial3Api::class)
    Card(
        onClick,
        modifier,
        border = BorderStroke(2.dp, MastodonteTheme.colorScheme.outlineVariant)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MastodonteTheme.colorScheme.outline,
            LocalTextStyle provides MastodonteTheme.typography.bodySmall
        ) {
            Row(
                Modifier.padding(MastodonteTheme.spacings.small),
                horizontalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    vector,
                    contentDescription,
                    Modifier
                        .clickable(onClick = onClick)
                        .size(18.dp)
                )

                ProvideTextStyle(MastodonteTheme.typography.bodySmall) {
                    label()
                }
            }
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
