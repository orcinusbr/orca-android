package com.jeanbarrossilva.orca.platform.ui.component.stat

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

/** Tag for identifying an [ActivateableStatIcon] for testing purposes. **/
const val ACTIVATEABLE_STAT_ICON_TAG = "activateable-stat-icon"

/** Determines whether a [ActivateableStatIcon] is interactive. **/
sealed class ActivateableStatIconInteractiveness {
    /** Mode of non-interactivity. **/
    object Still : ActivateableStatIconInteractiveness() {
        override fun onInteraction(isActive: Boolean) {
        }
    }

    /**
     * Mode of interactivity in which the [ActivateableStatIcon] can be toggled.
     *
     * @param onInteraction Callback run whenever it's clicked, signalling a request for the state this
     * [ActivateableStatIcon] represents to be switched.
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
     * @param isActive Whether the [ActivateableStatIcon] is active.
     **/
    internal abstract fun onInteraction(isActive: Boolean)
}

/**
 * Provides [Color]s for coloring a [ActivateableStatIcon].
 *
 * @param inactiveColor [Color] by which the [ActivateableStatIcon] is colored when it's inactive.
 * @param activeColor [Color] by which the [ActivateableStatIcon] is colored when it's active.
 **/
@Immutable
class ActivateableStatIconColors internal constructor(
    private val inactiveColor: Color,
    private val activeColor: Color
) {
    /**
     * Provides the [Color] that matches the activeness of the [ActivateableStatIcon] represented by
     * [isActive].
     *
     * @param isActive Whether the [ActivateableStatIcon] is active.
     **/
    internal fun color(isActive: Boolean): Color {
        return if (isActive) activeColor else inactiveColor
    }
}

/**
 * A stat [Icon] that can be in an active state.
 *
 * @param vector [ImageVector] that's the icon to be displayed.
 * @param contentDescription Shortly describes the operation this [ActivateableStatIcon] represents.
 * @param isActive Whether the state it represents is enabled.
 * @param interactiveness [ActivateableStatIconInteractiveness] that indicates whether this
 * [ActivateableStatIcon] can be interacted with.
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
        spring(Spring.DampingRatioMediumBouncy)
    )
    val tint by animateColorAsState(colors.color(isActive))

    Icon(
        vector,
        contentDescription,
        modifier
            .pointerInput(isActive) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        try { awaitRelease() } catch (_: GestureCancellationException) { }
                        isPressed = false
                        interactiveness.onInteraction(!isActive)
                    }
                )
            }
            .scale(scale)
            .testTag(ACTIVATEABLE_STAT_ICON_TAG)
            .semantics { selected = isActive },
        tint
    )
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun InactiveActivateableStatIconPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colorScheme.background) {
            ActivateableStatIcon(isActive = false)
        }
    }
}

@Composable
@Preview
private fun ActiveActivateableStatIconPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colorScheme.background) {
            ActivateableStatIcon(isActive = true)
        }
    }
}

@Composable
private fun ActivateableStatIcon(isActive: Boolean, modifier: Modifier = Modifier) {
    ActivateableStatIcon(
        OrcaTheme.Icons.WifiOff,
        contentDescription = "Disconnect",
        isActive,
        ActivateableStatIconInteractiveness.Still,
        ActivateableStatIconColors(LocalContentColor.current, Color.Green),
        modifier
    )
}
