package com.jeanbarrossilva.mastodonte.feature.tootdetails.ui.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

@Composable
internal fun Stat(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier,
        Arrangement.spacedBy(MastodonteTheme.spacings.small),
        Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MastodonteTheme.colorScheme.outline,
            LocalTextStyle provides MastodonteTheme.typography.bodySmall
        ) {
            content()
        }
    }
}

@Composable
@Preview
private fun StatPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            Stat {
                Icon(MastodonteTheme.Icons.ThumbUp, contentDescription = "Likes")
                Text("8")
            }
        }
    }
}
