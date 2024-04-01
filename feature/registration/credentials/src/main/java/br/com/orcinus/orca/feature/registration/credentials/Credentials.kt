/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.feature.registration.credentials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.platform.autos.kit.action.button.PrimaryButton
import br.com.orcinus.orca.platform.autos.kit.input.text.FormTextField
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.plus
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

@Composable
internal fun Credentials(viewModel: CredentialsViewModel, modifier: Modifier = Modifier) {
  val email by viewModel.emailFlow.collectAsState()
  val password by viewModel.passwordFlow.collectAsState()

  Credentials(
    email,
    onEmailChange = viewModel::setEmail,
    password,
    onPasswordChange = viewModel::setPassword,
    modifier
  )
}

@Composable
private fun Credentials(
  email: String,
  onEmailChange: (email: String) -> Unit,
  password: String,
  onPasswordChange: (password: String) -> Unit,
  modifier: Modifier = Modifier
) {
  val spacing = AutosTheme.spacings.large.dp
  val spacerModifier = remember { Modifier.height(spacing) }

  Scaffold(
    modifier,
    bottom = {
      ButtonBar {
        PrimaryButton(onClick = {}) {
          Text(stringResource(R.string.feature_registration_credentials_confirm))
        }
      }
    }
  ) {
    expanded {
      LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = it + PaddingValues(spacing),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        item { Spacer(spacerModifier) }

        item {
          Column(
            Modifier.fillMaxWidth(),
            Arrangement.spacedBy(AutosTheme.spacings.medium.dp),
            Alignment.CenterHorizontally
          ) {
            Text(
              stringResource(R.string.feature_registration_credentials),
              textAlign = TextAlign.Center,
              style = AutosTheme.typography.headlineLarge
            )

            Text(
              stringResource(R.string.feature_registration_credentials_explanation),
              textAlign = TextAlign.Center,
              style = AutosTheme.typography.headlineSmall
            )
          }
        }

        item {
          Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.small.dp)) {
            FormTextField(email, onEmailChange) {
              Text(stringResource(R.string.feature_registration_credentials_email))
            }

            FormTextField(password, onPasswordChange) {
              Text(stringResource(R.string.feature_registration_credentials_password))
            }
          }
        }

        item { Spacer(spacerModifier) }
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun CredentialsPreview() {
  AutosTheme {
    Credentials(
      email = "jean@orcinus.com.br",
      onEmailChange = {},
      password = "password123",
      onPasswordChange = {}
    )
  }
}
