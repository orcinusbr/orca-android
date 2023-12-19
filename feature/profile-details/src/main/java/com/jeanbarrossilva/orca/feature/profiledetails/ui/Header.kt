/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.feature.profiledetails.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetails
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.avatar.LargeAvatar
import com.jeanbarrossilva.orca.std.image.compose.SomeComposableImageLoader

@Composable
internal fun Header(modifier: Modifier = Modifier) {
  Header(
    avatar = { LargeAvatar() },
    name = { MediumTextualPlaceholder() },
    account = { LargeTextualPlaceholder() },
    bio = {
      Column(
        verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.extraSmall.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        repeat(3) { LargeTextualPlaceholder() }
        MediumTextualPlaceholder()
      }
    },
    mainActionButton = {},
    modifier
  )
}

@Composable
internal fun Header(details: ProfileDetails, modifier: Modifier = Modifier) {
  Header(
    avatar = { LargeAvatar(details.avatarLoader as SomeComposableImageLoader, details.name) },
    name = { Text(details.name) },
    account = { Text(details.formattedAccount) },
    bio = { Text(details.bio) },
    mainActionButton = { details.MainActionButton() },
    modifier
  )
}

@Composable
private fun Header(
  avatar: @Composable () -> Unit,
  name: @Composable () -> Unit,
  account: @Composable () -> Unit,
  bio: @Composable () -> Unit,
  mainActionButton: @Composable () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier.padding(AutosTheme.spacings.extraLarge.dp).fillMaxWidth(),
    Arrangement.spacedBy(AutosTheme.spacings.extraLarge.dp),
    Alignment.CenterHorizontally
  ) {
    avatar()

    Column(
      verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.extraSmall.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      ProvideTextStyle(AutosTheme.typography.headlineLarge, name)
      ProvideTextStyle(AutosTheme.typography.titleSmall, account)
    }

    ProvideTextStyle(LocalTextStyle.current.copy(textAlign = TextAlign.Center), bio)
    mainActionButton()
  }
}

@Composable
@MultiThemePreview
private fun LoadingHeaderPreview() {
  AutosTheme { Surface(color = AutosTheme.colors.background.container.asColor) { Header() } }
}

@Composable
@MultiThemePreview
private fun HeaderPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      Header(ProfileDetails.sample)
    }
  }
}
