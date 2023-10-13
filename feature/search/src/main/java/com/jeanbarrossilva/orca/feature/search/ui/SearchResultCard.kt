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
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.sample
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.SmallAvatar
import com.jeanbarrossilva.orca.std.imageloader.compose.rememberImageLoader

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
  val avatarLoader = rememberImageLoader(searchResult.avatarURL)

  SearchResultCard(
    avatar = { SmallAvatar(avatarLoader, searchResult.name) },
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
  val horizontalSpacing = OrcaTheme.spacings.large

  Row(
    modifier
      .padding(horizontalSpacing, vertical = OrcaTheme.spacings.medium)
      .clickable(onClick = onClick)
      .fillMaxWidth()
      .semantics { role = Role.Button },
    Arrangement.spacedBy(horizontalSpacing),
    Alignment.CenterVertically
  ) {
    avatar()

    Column(verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.extraSmall)) {
      ProvideTextStyle(OrcaTheme.typography.bodyLarge, content = name)
      ProvideTextStyle(OrcaTheme.typography.bodyMedium, content = account)
    }
  }
}

@Composable
@MultiThemePreview
private fun LoadingSearchResultCardPreview() {
  OrcaTheme { Surface(color = OrcaTheme.colors.background.container) { SearchResultCard() } }
}

@Composable
@MultiThemePreview
private fun LoadedSearchResultCardPreview() {
  OrcaTheme {
    Surface(color = OrcaTheme.colors.background.container) {
      SearchResultCard(ProfileSearchResult.sample, onClick = {})
    }
  }
}
