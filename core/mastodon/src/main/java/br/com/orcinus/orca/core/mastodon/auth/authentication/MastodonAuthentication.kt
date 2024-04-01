/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.auth.authentication

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.core.mastodon.R
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/**
 * Visually notifies that authentication is running in the background.
 *
 * @param modifier [Modifier] to be applied to the underlying [Surface].
 */
@Composable
internal fun MastodonAuthentication(modifier: Modifier = Modifier) {
  Surface(modifier, color = AutosTheme.colors.background.container.asColor) {
    Column(
      Modifier.fillMaxSize(),
      Arrangement.spacedBy(AutosTheme.spacings.medium.dp, Alignment.CenterVertically),
      Alignment.CenterHorizontally
    ) {
      Icon(
        AutosTheme.iconography.login.asImageVector,
        contentDescription = "Link",
        Modifier.size(64.dp)
      )

      Text(
        stringResource(R.string.core_http_authentication_authenticating),
        textAlign = TextAlign.Center,
        style = AutosTheme.typography.headlineLarge
      )
    }
  }
}

/** Preview of a [MastodonAuthentication]. */
@Composable
@MultiThemePreview
private fun MastodonAuthenticationPreview() {
  AutosTheme { MastodonAuthentication() }
}
