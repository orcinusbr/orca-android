package com.jeanbarrossilva.orca.platform.ui.component.stat.favorite

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIcon
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconColors
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconDefaults
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness

/** Tag that identifies a [FavoriteStatIcon] for testing purposes. **/
const val FAVORITE_STAT_ICON_TAG = "favorite-stat-icon"

/** Default values of a [FavoriteStatIcon]. **/
object FavoriteStatIconDefaults {
    /** [Color] by which a [FavoriteStatIcon] is colored by when it's active. **/
    val ActiveColor = Color(0xFFD32F2F)

    /**
     * [ActivateableStatIconColors] by which a [FavoriteStatIcon] is colored by default.
     *
     * @param inactiveColor [Color] to color it with when it's inactive.
     * @param activeColor [Color] to color it with when it's active.
     **/
    @Composable
    fun colors(inactiveColor: Color = LocalContentColor.current, activeColor: Color = ActiveColor):
        ActivateableStatIconColors {
        return ActivateableStatIconColors(inactiveColor, activeColor)
    }
}

/**
 * [ActivateableStatIconDefaults] that represents a "favorite" stat.
 *
 * @param isActive Whether the state it represents is enabled.
 * @param interactiveness [ActivateableStatIconInteractiveness] that indicates whether this
 * [ActivateableStatIconDefaults] can be interacted with.
 * @param colors [ActivateableStatIconColors] that defines the [Color]s to color it.
 * @param modifier [Modifier] to be applied to the underlying [ActivateableStatIconDefaults].
 **/
@Composable
fun FavoriteStatIcon(
    isActive: Boolean,
    interactiveness: ActivateableStatIconInteractiveness,
    modifier: Modifier = Modifier,
    colors: ActivateableStatIconColors = FavoriteStatIconDefaults.colors()
) {
    ActivateableStatIcon(
        if (isActive) {
            OrcaTheme.iconography.favorite.filled
        } else {
            OrcaTheme.iconography.favorite.outlined
        },
        contentDescription = "Favorite",
        isActive,
        interactiveness,
        colors,
        modifier.testTag(FAVORITE_STAT_ICON_TAG)
    )
}

@Composable
@MultiThemePreview
private fun InactiveFavoriteStatIconPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background) {
            FavoriteStatIcon(isActive = false, ActivateableStatIconInteractiveness.Still)
        }
    }
}

@Composable
@MultiThemePreview
private fun ActiveFavoriteStatIconPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background) {
            FavoriteStatIcon(isActive = true, ActivateableStatIconInteractiveness.Still)
        }
    }
}
