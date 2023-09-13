package com.jeanbarrossilva.orca.platform.ui.component.stat

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

/** Tag for identifying an [ActivateableStatIconDefaults] for testing purposes. **/
const val ACTIVATEABLE_STAT_ICON_TAG = "activateable-stat-icon"

/** Default values of an [ActivateableStatIconDefaults]. **/
internal object ActivateableStatIconDefaults {
    /** Default size of an [ActivateableStatIconDefaults]. **/
    val Size = 18.dp
}

/** Determines whether a [ActivateableStatIconDefaults] is interactive. **/
sealed class ActivateableStatIconInteractiveness {
    /** Mode of non-interactivity. **/
    object Still : ActivateableStatIconInteractiveness() {
        override fun onInteraction(isActive: Boolean) {
        }
    }

    /**
     * Mode of interactivity in which the [ActivateableStatIconDefaults] can be toggled.
     *
     * @param onInteraction Callback run whenever it's clicked, signalling a request for the state this
     * [ActivateableStatIconDefaults] represents to be switched.
     **/
    class Interactive(private val onInteraction: (isActive: Boolean) -> Unit) :
        ActivateableStatIconInteractiveness() {
        override fun onInteraction(isActive: Boolean) {
            onInteraction.invoke(isActive)
        }
    }

    /**
     * Performs an action based on the received interaction.
     *
     * @param isActive Whether the [ActivateableStatIconDefaults] is active.
     **/
    internal abstract fun onInteraction(isActive: Boolean)
}

/**
 * Provides [Color]s for coloring a [ActivateableStatIconDefaults].
 *
 * @param inactiveColor [Color] by which the [ActivateableStatIconDefaults] is colored when it's inactive.
 * @param activeColor [Color] by which the [ActivateableStatIconDefaults] is colored when it's active.
 **/
@Immutable
class ActivateableStatIconColors internal constructor(
    private val inactiveColor: Color,
    private val activeColor: Color
) {
    /**
     * Provides the [Color] that matches the activeness of the [ActivateableStatIconDefaults] represented by
     * [isActive].
     *
     * @param isActive Whether the [ActivateableStatIconDefaults] is active.
     **/
    internal fun color(isActive: Boolean): Color {
        return if (isActive) activeColor else inactiveColor
    }
}

/**
 * A stat [Icon] that can be in an active state.
 *
 * @param vector [ImageVector] that's the icon to be displayed.
 * @param contentDescription Shortly describes the operation this [ActivateableStatIconDefaults] represents.
 * @param isActive Whether the state it represents is enabled.
 * @param interactiveness [ActivateableStatIconInteractiveness] that indicates whether this
 * [ActivateableStatIconDefaults] can be interacted with.
 * @param colors [ActivateableStatIconColors] that defines the [Color]s to color it.
 * @param modifier [Modifier] to be applied to the underlying [Icon].
 **/
@Composable
internal fun ActivateableStatIcon(
    vector: ImageVector,
    contentDescription: String,
    isActive: Boolean,
    interactiveness: ActivateableStatIconInteractiveness,
    colors: ActivateableStatIconColors,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        if (isPressed) 1.5f else 1f,
        spring(Spring.DampingRatioMediumBouncy),
        label = "Scale"
    )
    val tint by animateColorAsState(colors.color(isActive), label = "Tint")

    Icon(
        vector,
        contentDescription,
        modifier
            .pointerInput(isActive) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                ) {
                    interactiveness.onInteraction(!isActive)
                }
            }
            .scale(scale)
            .size(ActivateableStatIconDefaults.Size)
            .testTag(ACTIVATEABLE_STAT_ICON_TAG)
            .semantics { selected = isActive },
        tint
    )
}

@Composable
@MultiThemePreview
private fun InactiveActivateableStatIconPreview() {
    OrcaTheme {
        ActivateableStatIcon(isActive = false)
    }
}

@Composable
@MultiThemePreview
private fun ActiveActivateableStatIconPreview() {
    OrcaTheme {
        ActivateableStatIcon(isActive = true)
    }
}

@Composable
private fun ActivateableStatIcon(isActive: Boolean, modifier: Modifier = Modifier) {
    ActivateableStatIcon(
        OrcaTheme.iconography.link,
        contentDescription = "Disconnect",
        isActive,
        ActivateableStatIconInteractiveness.Still,
        ActivateableStatIconColors(
            inactiveColor = LocalContentColor.current,
            activeColor = OrcaTheme.colors.link
        ),
        modifier
    )
}
