package com.jeanbarrossilva.orca.feature.tootdetails.ui.header.stat

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetails
import com.jeanbarrossilva.orca.feature.tootdetails.ui.header.Stat
import com.jeanbarrossilva.orca.feature.tootdetails.ui.header.StatDefaults
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.favorite.FavoriteStatIcon

@Composable
internal fun FavoriteStat(
  details: TootDetails,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isActive = remember(details, details::isFavorite)
  val contentColor by
    animateColorAsState(
      if (isActive) OrcaTheme.colors.activation.favorite else StatDefaults.contentColor,
      label = "ContentColor"
    )

  Stat(contentColor = contentColor) {
    FavoriteStatIcon(
      isActive,
      ActivateableStatIconInteractiveness.Interactive { onClick() },
      modifier.size(24.dp)
    )

    Text(details.formattedFavoriteCount)
  }
}

@Composable
@MultiThemePreview
private fun InactiveFavoriteStatPreview() {
  OrcaTheme { FavoriteStat(TootDetails.sample.copy(isFavorite = false), onClick = {}) }
}

@Composable
@MultiThemePreview
private fun ActiveFavoriteStatPreview() {
  OrcaTheme { FavoriteStat(TootDetails.sample.copy(isFavorite = true), onClick = {}) }
}
