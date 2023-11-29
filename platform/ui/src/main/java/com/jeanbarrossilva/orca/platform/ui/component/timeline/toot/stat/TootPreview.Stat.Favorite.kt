package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.favorite.FavoriteStatIcon
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.Stat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.StatDefaults
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.StatPosition
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview

/** Tag that identifies a [TootPreview]'s favorite count stat for testing purposes. */
const val TOOT_PREVIEW_FAVORITE_STAT_TAG = "toot-preview-favorites-stat"

@Composable
internal fun FavoriteStat(
  position: StatPosition,
  preview: TootPreview,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isActive = remember(preview) { preview.isFavorite }

  Stat(position, onClick, modifier.testTag(TOOT_PREVIEW_FAVORITE_STAT_TAG)) {
    val contentColor by
      animateColorAsState(
        if (isActive) AutosTheme.colors.activation.favorite.asColor else LocalContentColor.current,
        label = "ContentColor"
      )

    FavoriteStatIcon(
      isActive,
      ActivateableStatIconInteractiveness.Interactive { onClick() },
      Modifier.size(StatDefaults.IconSize)
    )

    Text(preview.formattedFavoriteCount, color = contentColor)
  }
}

@Composable
@MultiThemePreview
private fun InactiveFavoriteStatPreview() {
  AutosTheme {
    FavoriteStat(StatPosition.SUBSEQUENT, TootPreview.sample.copy(isFavorite = false), onClick = {})
  }
}

@Composable
@MultiThemePreview
private fun ActiveFavoriteStatPreview() {
  AutosTheme {
    FavoriteStat(StatPosition.SUBSEQUENT, TootPreview.sample.copy(isFavorite = true), onClick = {})
  }
}
