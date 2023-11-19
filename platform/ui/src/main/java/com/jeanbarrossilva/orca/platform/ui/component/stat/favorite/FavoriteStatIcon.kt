package com.jeanbarrossilva.orca.platform.ui.component.stat.favorite

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIcon
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconColors
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconDefaults
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness

/** Tag that identifies a [FavoriteStatIcon] for testing purposes. */
const val FAVORITE_STAT_ICON_TAG = "favorite-stat-icon"

/** Default values of a [FavoriteStatIcon]. */
object FavoriteStatIconDefaults {
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
 * [ActivateableStatIconDefaults] that represents a "favorite" stat.
 *
 * @param isActive Whether the state it represents is enabled.
 * @param interactiveness [ActivateableStatIconInteractiveness] that indicates whether this
 *   [ActivateableStatIconDefaults] can be interacted with.
 * @param colors [ActivateableStatIconColors] that defines the [Color]s to color it.
 * @param modifier [Modifier] to be applied to the underlying [ActivateableStatIconDefaults].
 */
@Composable
fun FavoriteStatIcon(
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
    contentDescription = stringResource(R.string.platform_ui_favorite_stat),
    isActive,
    interactiveness,
    colors,
    modifier.testTag(FAVORITE_STAT_ICON_TAG)
  )
}

@Composable
@MultiThemePreview
private fun InactiveFavoriteStatIconPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      FavoriteStatIcon(isActive = false, ActivateableStatIconInteractiveness.Still)
    }
  }
}

@Composable
@MultiThemePreview
private fun ActiveFavoriteStatIconPreview() {
  AutosTheme {
    Surface(color = AutosTheme.colors.background.container.asColor) {
      FavoriteStatIcon(isActive = true, ActivateableStatIconInteractiveness.Still)
    }
  }
}
