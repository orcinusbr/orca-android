package com.jeanbarrossilva.orca.core.mastodon.auth.authorization.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

@Composable
internal fun Authorization(modifier: Modifier = Modifier) {
    Surface(modifier, color = OrcaTheme.colors.background) {
        Column(
            Modifier.fillMaxSize(),
            Arrangement.spacedBy(OrcaTheme.spacings.medium, Alignment.CenterVertically),
            Alignment.CenterHorizontally
        ) {
            Icon(OrcaTheme.iconography.login, contentDescription = "Link", Modifier.size(64.dp))

            Text(
                "Authorizing...",
                textAlign = TextAlign.Center,
                style = OrcaTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
@MultiThemePreview
private fun AuthorizationPreview() {
    OrcaTheme {
        Authorization()
    }
}
