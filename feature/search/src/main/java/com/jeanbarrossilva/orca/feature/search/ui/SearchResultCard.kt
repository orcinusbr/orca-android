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

package com.jeanbarrossilva.orca.feature.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.orca.composite.timeline.avatar.SmallAvatar
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.core.sample
import com.jeanbarrossilva.orca.std.image.compose.SomeComposableImageLoader

@Composable
internal fun SearchResultCard(modifier: Modifier = Modifier) {
  SearchResultCard(
    avatar = { SmallAvatar() },
    name = { MediumTextualPlaceholder() },
    account = { SmallTextualPlaceholder() },
    onClick = {},
    modifier
  )
}

@Composable
internal fun SearchResultCard(
  searchResult: ProfileSearchResult,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  SearchResultCard(
    avatar = {
      SmallAvatar(searchResult.avatarLoader as SomeComposableImageLoader, searchResult.name)
    },
    name = { Text(searchResult.name) },
    account = { Text("${searchResult.account}") },
    onClick,
    modifier
  )
}

@Composable
private fun SearchResultCard(
  avatar: @Composable () -> Unit,
  name: @Composable () -> Unit,
  account: @Composable () -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val horizontalSpacing = AutosTheme.spacings.large.dp

  Row(
    modifier
      .padding(horizontalSpacing, vertical = AutosTheme.spacings.medium.dp)
      .clickable(onClick = onClick)
      .fillMaxWidth()
      .semantics { role = Role.Button },
    Arrangement.spacedBy(horizontalSpacing),
    Alignment.CenterVertically
  ) {
    avatar()

    Column(verticalArrangement = Arrangement.spacedBy(AutosTheme.spacings.extraSmall.dp)) {
      ProvideTextStyle(AutosTheme.typography.bodyLarge, content = name)
      ProvideTextStyle(AutosTheme.typography.bodyMedium, content = account)
    }
  }
}

@Composable
@MultiThemePreview
private fun LoadingSearchResultCardPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) { SearchResultCard() }
  }
}

@Composable
@MultiThemePreview
private fun LoadedSearchResultCardPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      SearchResultCard(ProfileSearchResult.sample, onClick = {})
    }
  }
}
