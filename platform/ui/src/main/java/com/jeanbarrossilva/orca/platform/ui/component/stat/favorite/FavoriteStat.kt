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

package com.jeanbarrossilva.orca.platform.ui.component.stat.favorite

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.Stat
import com.jeanbarrossilva.orca.platform.ui.component.stat.StatDefaults
import com.jeanbarrossilva.orca.platform.ui.component.stat.StatPosition
import com.jeanbarrossilva.orca.platform.ui.component.stat.StatsDetails
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview

/** Tag that identifies a [PostPreview]'s favorite count stat for testing purposes. */
const val POST_PREVIEW_FAVORITE_STAT_TAG = "post-preview-favorites-stat"

@Composable
internal fun FavoriteStat(
  position: StatPosition,
  details: StatsDetails,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  inactiveContentColor: Color = StatDefaults.contentColor
) {
  val isActive = remember(details, details::isFavorite)

  Stat(position, onClick, modifier.testTag(POST_PREVIEW_FAVORITE_STAT_TAG)) {
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
      StatPosition.SUBSEQUENT,
      StatsDetails.sample.copy(isFavorite = false),
      onClick = {}
    )
  }
}

@Composable
@MultiThemePreview
private fun ActiveFavoriteStatPreview() {
  AutosTheme {
    FavoriteStat(StatPosition.SUBSEQUENT, StatsDetails.sample.copy(isFavorite = true), onClick = {})
  }
}
