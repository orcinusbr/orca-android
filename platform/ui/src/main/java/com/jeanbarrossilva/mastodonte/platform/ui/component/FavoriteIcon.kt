package com.jeanbarrossilva.mastodonte.platform.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.rounded.Favorite
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.Favorite

object FavoriteIconDefaults {
    @Composable
    fun colors(
        inactiveColor: Color = LocalContentColor.current,
        activeColor: Color = MastodonteTheme.colorScheme.error
    ): FavoriteIconColors {
        return FavoriteIconColors(inactiveColor, activeColor)
    }
}

@Immutable
class FavoriteIconColors internal constructor(
    private val inactiveColor: Color,
    private val activeColor: Color
) {
    internal fun color(isActive: Boolean): Color {
        return if (isActive) activeColor else inactiveColor
    }
}

@Composable
fun FavoriteIcon(
    isActive: Boolean,
    onToggle: (isActive: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    colors: FavoriteIconColors = FavoriteIconDefaults.colors()
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        if (isPressed) 1.5f else 1f,
        spring(Spring.DampingRatioMediumBouncy)
    )
    val tint by animateColorAsState(colors.color(isActive))

    Icon(
        MastodonteTheme.Icons.Favorite,
        contentDescription = "Favorite",
        modifier
            .pointerInput(isActive) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        try { awaitRelease() } catch (_: GestureCancellationException) { }
                        isPressed = false
                        onToggle(!isActive)
                    }
                )
            }
            .scale(scale)
            .semantics { set(SemanticsProperties.Favorite, isActive) },
        tint
    )
}

@Composable
@Preview
private fun InactiveFavoriteIconPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            FavoriteIcon(isActive = false, onToggle = { })
        }
    }
}

@Composable
@Preview
private fun ActiveFavoriteIconPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            FavoriteIcon(isActive = true, onToggle = { })
        }
    }
}
