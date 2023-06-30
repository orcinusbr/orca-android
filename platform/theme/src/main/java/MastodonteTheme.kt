package com.jeanbarrossilva.mastodonte.platform.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jeanbarrossilva.mastodonte.platform.theme.configuration.LocalOverlays
import com.jeanbarrossilva.mastodonte.platform.theme.configuration.LocalSpacings
import com.jeanbarrossilva.mastodonte.platform.theme.configuration.Overlays
import com.jeanbarrossilva.mastodonte.platform.theme.configuration.Spacings
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.Rubik
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.colorAttribute
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.with

/** [Color] for hierarchically lower non-highlighted content. **/
private val fadedContentColor
    @Composable get() = colorAttribute(android.R.attr.textColorTertiaryInverse)

/** [Color] for non-highlighted content. **/
private val defaultContentColor
    @Composable get() = colorAttribute(android.R.attr.textColorSecondaryInverse)

/** Provider of [MastodonteTheme]'s configurations. **/
object MastodonteTheme {
    /**
     * [Icons][androidx.compose.material.icons.Icons] in the chosen style. Alias for
     * [Icons.Rounded].
     **/
    val Icons = androidx.compose.material.icons.Icons.Rounded

    /** [Current][CompositionLocal.current] [ColorScheme] from the underlying [MaterialTheme]. **/
    val colorScheme
        @Composable get() = MaterialTheme.colorScheme

    /** [Current][CompositionLocal.current] [Overlays] from [LocalOverlays]. **/
    val overlays
        @Composable get() = LocalOverlays.current

    /** [Current][CompositionLocal.current] [Shapes] from the underlying [MaterialTheme]. **/
    val shapes
        @Composable get() = MaterialTheme.shapes

    /** [Current][CompositionLocal.current] [Spacings] from [LocalSpacings]. **/
    val spacings
        @Composable get() = LocalSpacings.current

    /** [Current][CompositionLocal.current] [Typography] from the underlying [MaterialTheme]. **/
    val typography
        @Composable get() = MaterialTheme.typography
}

/**
 * [MaterialTheme] for Mastodonte.
 *
 * @param content Content to be themed.
 **/
@Composable
fun MastodonteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primaryContainer = colorAttribute(
                com.google.android.material.R.attr.colorPrimaryContainer
            ),
            onPrimaryContainer = colorAttribute(
                com.google.android.material.R.attr.colorOnPrimaryContainer
            ),
            background = colorAttribute(com.google.android.material.R.attr.backgroundColor),
            surface = colorAttribute(com.google.android.material.R.attr.colorSurface),
            surfaceVariant = colorAttribute(com.google.android.material.R.attr.colorSurfaceVariant),
            onSurfaceVariant = colorAttribute(
                com.google.android.material.R.attr.colorOnSurfaceVariant
            ),
            error = colorAttribute(com.google.android.material.R.attr.colorError),
            errorContainer = colorAttribute(com.google.android.material.R.attr.colorErrorContainer),
            onErrorContainer = colorAttribute(
                com.google.android.material.R.attr.colorOnErrorContainer
            ),
            outline = colorAttribute(com.google.android.material.R.attr.colorOutline),
            outlineVariant = colorAttribute(com.google.android.material.R.attr.colorOutlineVariant)
        ),
        typography = with(Typography() with FontFamily.Rubik) {
            copy(
                displayLarge = displayLarge.copy(fontWeight = FontWeight.Black),
                headlineLarge = headlineLarge.copy(fontWeight = FontWeight.Black),
                titleLarge = titleLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold),
                titleMedium = titleSmall.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                titleSmall = titleSmall.copy(color = defaultContentColor, fontSize = 16.sp),
                bodyLarge = bodyLarge.copy(fontWeight = FontWeight.Bold),
                bodyMedium = bodyMedium.copy(fontSize = 14.sp),
                bodySmall = bodySmall
                    .copy(color = fadedContentColor, fontWeight = FontWeight.Medium)
            )
        }
    ) {
        CompositionLocalProvider(
            LocalOverlays provides Overlays.Default,
            LocalSpacings provides Spacings.default,
            LocalTextStyle provides MastodonteTheme.typography.bodyMedium,
            content = content
        )
    }
}
