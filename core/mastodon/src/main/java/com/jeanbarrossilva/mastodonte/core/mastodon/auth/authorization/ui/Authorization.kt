package com.jeanbarrossilva.mastodonte.core.mastodon.auth.authorization.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

@Composable
internal fun Authorization(modifier: Modifier = Modifier) {
    Surface(modifier, color = MastodonteTheme.colorScheme.background) {
        Column(
            Modifier.fillMaxSize(),
            Arrangement.spacedBy(MastodonteTheme.spacings.medium, Alignment.CenterVertically),
            Alignment.CenterHorizontally
        ) {
            Icon(MastodonteTheme.Icons.Link, contentDescription = "Link", Modifier.size(64.dp))

            Text(
                "Authorizing...",
                textAlign = TextAlign.Center,
                style = MastodonteTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
@Preview
private fun AuthorizationPreview() {
    MastodonteTheme {
        Authorization()
    }
}
