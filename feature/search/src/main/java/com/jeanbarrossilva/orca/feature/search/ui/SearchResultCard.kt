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
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.createSample
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.avatar.SmallAvatar
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

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
    avatar = { SmallAvatar(searchResult.avatarLoader, searchResult.name) },
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
      SearchResultCard(
        ProfileSearchResult.createSample(ImageLoader.Provider.createSample()),
        onClick = {}
      )
    }
  }
}
