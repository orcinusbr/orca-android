package com.jeanbarrossilva.orca.platform.theme.configuration.colors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.jeanbarrossilva.orca.platform.theme.R
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors.Activation
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.contrast.Contrast
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.contrast.and

/** [CompositionLocal] that provides [Colors]. **/
internal val LocalColors = compositionLocalOf {
    Colors.Unspecified
}

/**
 * [Contrast]s and [Color]s that compose the palette to be used by the application.
 *
 * @param activation [Activation] for representing various activation states.
 * @param background [Color] for the overall background.
 * @param brand [Contrast] that uses the primary [Color] of Orca.
 * @param disabled [Contrast] for components in a disabled state.
 * @param error [Contrast] for error states.
 * @param placeholder [Color] for components that indicate loading content.
 * @param primary [Color] for primary components.
 * @param secondary [Color] for secondary components.
 * @param surface [Contrast] for on-background or nested containers.
 * @param tertiary [Color] for tertiary components.
 **/
@Immutable
data class Colors internal constructor(
    val activation: Activation,
    val background: Color,
    val brand: Contrast,
    val disabled: Contrast,
    val error: Contrast,
    val placeholder: Color,
    val primary: Color,
    val secondary: Color,
    val surface: Contrast,
    val tertiary: Color
) {
    /**
     * [Contrast]s representing activation states.
     *
     * @param favorite [Contrast] for a "favorited" state.
     * @param reblog [Contrast] for a "reblogged" state.
     **/
    @Immutable
    data class Activation internal constructor(val favorite: Contrast, val reblog: Contrast) {
        companion object {
            /** [Activation] with [Contrast.Unspecified] values. **/
            internal val Unspecified =
                Activation(favorite = Contrast.Unspecified, reblog = Contrast.Unspecified)

            /** [Activation] that's provided by default. **/
            internal val default
                @Composable get() =
                    Activation(Color(0xFFD32F2F) and Color.White, Color(0xFF81C784) and Color.White)
        }
    }

    companion object {
        /** [Colors] with unspecified values. **/
        internal val Unspecified = Colors(
            Activation.Unspecified,
            background = Color.Unspecified,
            brand = Contrast.Unspecified,
            disabled = Contrast.Unspecified,
            error = Contrast.Unspecified,
            placeholder = Color.Unspecified,
            primary = Color.Unspecified,
            secondary = Color.Unspecified,
            surface = Contrast.Unspecified,
            tertiary = Color.Unspecified
        )

        /** [Colors] that are provided by default. **/
        internal val default
            @Composable get() = Colors(
                Activation.default,
                colorResource(R.color.background),
                colorResource(R.color.brandContainer) and colorResource(R.color.brandContent),
                colorResource(R.color.disabledContainer) and colorResource(R.color.disabledContent),
                colorResource(R.color.errorContainer) and colorResource(R.color.errorContent),
                colorResource(R.color.placeholder),
                colorResource(R.color.primary),
                colorResource(R.color.secondary),
                colorResource(R.color.surfaceContainer) and colorResource(R.color.surfaceContent),
                colorResource(R.color.tertiary)
            )
    }
}
