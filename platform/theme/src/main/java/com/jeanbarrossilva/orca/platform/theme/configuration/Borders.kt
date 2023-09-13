package com.jeanbarrossilva.orca.platform.theme.configuration

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors

/** [CompositionLocal] that provides [Borders]. **/
internal val LocalBorders = compositionLocalOf {
    Borders.Unspecified
}

/**
 * [BorderStroke]s by which components are bordered.
 *
 * @param medium [BorderStroke] that's the default one.
 **/
@Immutable
data class Borders internal constructor(val medium: BorderStroke) {
    companion object {
        /** Width of the default [medium] border. **/
        @Deprecated(
            "Prefer referring to OrcaTheme when in Compose.",
            ReplaceWith(
                "OrcaTheme.borders.medium.width",
                "com.jeanbarrossilva.orca.platform.theme.OrcaTheme"
            )
        )
        internal const val DefaultMediumWidth = 2

        /** [Borders] with unspecified values. **/
        internal val Unspecified =
            Borders(medium = BorderStroke(width = Dp.Unspecified, Color.Unspecified))

        /** [Borders] that are provided by default. **/
        internal val default
            @Composable
            @Suppress("DEPRECATION")
            get() = Borders(
                medium = BorderStroke(
                    DefaultMediumWidth.dp,
                    Color(getDefaultMediumColorInArgb(Colors.default))
                )
            )

        /** Whether components should have the [BorderStroke]s applied to them. **/
        internal val areApplicable
            @Composable get() = areApplicable(LocalContext.current)

        /**
         * Checks whether components should have the [BorderStroke]s applied to them.
         *
         * @param context [Context] through which the theme in which the system currently is will be
         * obtained.
         **/
        @JvmName("areApplicable")
        @JvmStatic
        internal fun areApplicable(context: Context): Boolean {
            return with(context.resources.configuration) {
                uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
            }
        }

        /**
         * Gets the [Color] by which the default [medium] border is colored.
         *
         * @param colors [Colors] by which it can be colored.
         **/
        @Deprecated(
            "Prefer referring to OrcaTheme when in Compose.",
            ReplaceWith(
                "(OrcaTheme.borders.medium.brush as SolidColor).value.toArgb()",
                "androidx.compose.ui.graphics.SolidColor",
                "androidx.compose.ui.graphics.toArgb",
                "com.jeanbarrossilva.orca.platform.theme.OrcaTheme"
            )
        )
        @JvmName("getDefaultMediumColorInArgb")
        @JvmStatic
        internal fun getDefaultMediumColorInArgb(colors: Colors): Int {
            return colors.placeholder.toArgb()
        }
    }
}
