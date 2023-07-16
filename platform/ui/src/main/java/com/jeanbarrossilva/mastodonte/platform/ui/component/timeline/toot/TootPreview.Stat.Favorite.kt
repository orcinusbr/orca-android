package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot

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
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.component.ActivateableStatIconInteractiveness
import com.jeanbarrossilva.mastodonte.platform.ui.component.stat.FavoriteStatIcon
import com.jeanbarrossilva.mastodonte.platform.ui.component.stat.FavoriteStatIconDefaults

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
        if (isActive) FavoriteStatIconDefaults.ActiveColor else StatDefaults.containerColor
    )

    Stat(
        onClick,
        modifier.testTag(TOOT_PREVIEW_FAVORITE_STAT_TAG),
        containerColor
    ) {
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
private fun InactiveFavoriteStatPreview() {
    MastodonteTheme {
        FavoriteStat(TootPreview.sample.copy(isFavorite = false), onClick = { })
    }
}

@Composable
@Preview
private fun ActiveFavoriteStatPreview() {
    MastodonteTheme {
        FavoriteStat(TootPreview.sample.copy(isFavorite = true), onClick = { })
    }
}
