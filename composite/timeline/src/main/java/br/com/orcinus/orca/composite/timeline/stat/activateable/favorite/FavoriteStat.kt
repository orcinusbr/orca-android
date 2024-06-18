/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.stat.activateable.favorite

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import br.com.orcinus.orca.composite.timeline.stat.Stat
import br.com.orcinus.orca.composite.timeline.stat.StatDefaults
import br.com.orcinus.orca.composite.timeline.stat.StatPosition
import br.com.orcinus.orca.composite.timeline.stat.activateable.ActivateableStatIconInteractiveness
import br.com.orcinus.orca.composite.timeline.stat.details.StatsDetails
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [FavoriteStat] for testing purposes. */
const val FAVORITE_STAT_TAG = "favorites-stat"

@Composable
internal fun FavoriteStat(
  position: StatPosition,
  details: StatsDetails,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  inactiveContentColor: Color = StatDefaults.contentColor
) {
  val isActive = remember(details, details::isFavorite)

  Stat(position, onClick, modifier.testTag(FAVORITE_STAT_TAG)) {
    val contentColor by
      animateColorAsState(
        if (isActive) AutosTheme.colors.activation.favorite.asColor else inactiveContentColor,
        label = "ContentColor"
      )

    FavoriteStatIcon(
      isActive,
      ActivateableStatIconInteractiveness.Interactive { onClick() },
      Modifier.size(StatDefaults.IconSize),
      FavoriteStatIconDefaults.colors(inactiveContentColor)
    )

    Text(details.formattedFavoriteCount, color = contentColor)
  }
}

@Composable
@MultiThemePreview
private fun InactiveFavoriteStatPreview() {
  AutosTheme {
    FavoriteStat(
      StatPosition.Subsequent,
      StatsDetails.sample.copy(isFavorite = false),
      onClick = {}
    )
  }
}

@Composable
@MultiThemePreview
private fun ActiveFavoriteStatPreview() {
  AutosTheme {
    FavoriteStat(StatPosition.Subsequent, StatsDetails.sample.copy(isFavorite = true), onClick = {})
  }
}
