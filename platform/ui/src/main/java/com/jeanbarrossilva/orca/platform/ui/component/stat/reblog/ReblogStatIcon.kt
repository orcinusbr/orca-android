package com.jeanbarrossilva.orca.platform.ui.component.stat.reblog

import android.content.res.Configuration
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIcon
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconColors
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconDefaults
import com.jeanbarrossilva.orca.platform.ui.component.stat.ActivateableStatIconInteractiveness

/** Tag that identifies a [ReblogStatIcon] for testing purposes. **/
const val REBLOG_STAT_ICON_TAG = "reblog-stat-icon"

/** Default values of a [ReblogStatIcon]. **/
object ReblogStatIconDefaults {
    /**
     * [ActivateableStatIconColors] by which a [ReblogStatIcon] is colored by default.
     *
     * @param inactiveColor [Color] to color it with when it's inactive.
     * @param activeColor [Color] to color it with when it's active.
     **/
    @Composable
    fun colors(
        inactiveColor: Color = LocalContentColor.current,
        activeColor: Color = Color(0xFF81C784)
    ): ActivateableStatIconColors {
        return ActivateableStatIconColors(inactiveColor, activeColor)
    }
}

/**
 * [ActivateableStatIconDefaults] that represents a "reblog" stat.
 *
 * @param isActive Whether the state it represents is enabled.
 * @param interactiveness [ActivateableStatIconInteractiveness] that indicates whether this
 * [ActivateableStatIconDefaults] can be interacted with.
 * @param colors [ActivateableStatIconColors] that defines the [Color]s to color it.
 * @param modifier [Modifier] to be applied to the underlying [ActivateableStatIconDefaults].
 **/
@Composable
fun ReblogStatIcon(
    isActive: Boolean,
    interactiveness: ActivateableStatIconInteractiveness,
    modifier: Modifier = Modifier,
    colors: ActivateableStatIconColors = ReblogStatIconDefaults.colors()
) {
    ActivateableStatIcon(
        OrcaTheme.Icons.Repeat,
        contentDescription = "Reblog",
        isActive,
        interactiveness,
        colors,
        modifier.testTag(REBLOG_STAT_ICON_TAG)
    )
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun InactiveReblogStatIconPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colorScheme.background) {
            ReblogStatIcon(isActive = false, ActivateableStatIconInteractiveness.Still)
        }
    }
}

@Composable
@Preview
private fun ActiveReblogStatIconPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colorScheme.background) {
            ReblogStatIcon(isActive = true, ActivateableStatIconInteractiveness.Still)
        }
    }
}
