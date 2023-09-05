package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.orca.platform.ui.component.stat.favorite.FavoriteStatIcon
import com.jeanbarrossilva.orca.platform.ui.component.stat.favorite.FavoriteStatIconDefaults
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.Stat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.StatDefaults
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview

/** Tag that identifies a [TootPreview]'s favorite count stat for testing purposes. **/
const val TOOT_PREVIEW_FAVORITE_STAT_TAG = "toot-preview-favorites-stat"

@Composable
internal fun FavoriteStat(
    preview: TootPreview,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isActive = remember(preview) { preview.isFavorite }
    val containerColor by animateColorAsState(
        if (isActive) FavoriteStatIconDefaults.ActiveColor else StatDefaults.containerColor,
        label = "ContainerColor"
    )

    Stat(onClick, modifier.testTag(TOOT_PREVIEW_FAVORITE_STAT_TAG), containerColor) {
        FavoriteStatIcon(
            isActive,
            ActivateableStatIconInteractiveness.Interactive { onClick() },
            Modifier.size(StatDefaults.IconSize),
            FavoriteStatIconDefaults.colors(activeColor = Color.White)
        )

        Text(preview.formattedFavoriteCount)
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun InactiveFavoriteStatPreview() {
    OrcaTheme {
        FavoriteStat(TootPreview.sample.copy(isFavorite = false), onClick = { })
    }
}

@Composable
@Preview
private fun ActiveFavoriteStatPreview() {
    OrcaTheme {
        FavoriteStat(TootPreview.sample.copy(isFavorite = true), onClick = { })
    }
}
