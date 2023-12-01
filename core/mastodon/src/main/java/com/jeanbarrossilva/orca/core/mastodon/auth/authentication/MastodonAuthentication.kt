/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.mastodon.auth.authentication

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
import com.jeanbarrossilva.orca.core.mastodon.R
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

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
