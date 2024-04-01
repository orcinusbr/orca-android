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

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import br.com.orcinus.orca.composite.timeline.R
import br.com.orcinus.orca.composite.timeline.stat.activateable.ActivateableStatIcon
import br.com.orcinus.orca.composite.timeline.stat.activateable.ActivateableStatIconColors
import br.com.orcinus.orca.composite.timeline.stat.activateable.ActivateableStatIconInteractiveness
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [FavoriteStatIcon] for testing purposes. */
const val FAVORITE_STAT_ICON_TAG = "favorite-stat-icon"

/** Default values of a [FavoriteStatIcon]. */
internal object FavoriteStatIconDefaults {
  /**
   * [ActivateableStatIconColors] by which a [FavoriteStatIcon] is colored by default.
   *
   * @param inactiveColor [Color] to color it with when it's inactive.
   * @param activeColor [Color] to color it with when it's active.
   */
  @Composable
  fun colors(
    inactiveColor: Color = LocalContentColor.current,
    activeColor: Color = AutosTheme.colors.activation.favorite.asColor
  ): ActivateableStatIconColors {
    return ActivateableStatIconColors(inactiveColor, activeColor)
  }
}

/**
 * [ActivateableStatIcon] of a [FavoriteStat].
 *
 * @param modifier [Modifier] to be applied to the underlying [ActivateableStatIcon].
 */
@Composable
internal fun FavoriteStatIcon(modifier: Modifier = Modifier) {
  FavoriteStatIcon(isActive = false, ActivateableStatIconInteractiveness.Still, modifier)
}

/**
 * [ActivateableStatIcon] of a [FavoriteStat].
 *
 * @param isActive Whether the state it represents is enabled.
 * @param interactiveness [ActivateableStatIconInteractiveness] that indicates whether it can be
 *   interacted with.
 * @param colors [ActivateableStatIconColors] that defines the [Color]s to color it.
 * @param modifier [Modifier] to be applied to the underlying [ActivateableStatIcon].
 */
@Composable
internal fun FavoriteStatIcon(
  isActive: Boolean,
  interactiveness: ActivateableStatIconInteractiveness,
  modifier: Modifier = Modifier,
  colors: ActivateableStatIconColors = FavoriteStatIconDefaults.colors()
) {
  ActivateableStatIcon(
    if (isActive) {
      AutosTheme.iconography.favorite.filled.asImageVector
    } else {
      AutosTheme.iconography.favorite.outlined.asImageVector
    },
    contentDescription = stringResource(R.string.composite_timeline_stat_favorite),
    isActive,
    interactiveness,
    colors,
    modifier.testTag(FAVORITE_STAT_ICON_TAG)
  )
}

/** Preview of an inactive [FavoriteStatIcon]. */
@Composable
@MultiThemePreview
private fun InactiveFavoriteStatIconPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      FavoriteStatIcon(isActive = false, ActivateableStatIconInteractiveness.Still)
    }
  }
}

/** Preview of an active [FavoriteStatIcon]. */
@Composable
@MultiThemePreview
private fun ActiveFavoriteStatIconPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      FavoriteStatIcon(isActive = true, ActivateableStatIconInteractiveness.Still)
    }
  }
}
