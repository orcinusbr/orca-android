package com.jeanbarrossilva.orca.platform.theme.configuration.colors

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.jeanbarrossilva.orca.platform.theme.R
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors.Activation
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.contrast.Contrast
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.contrast.and
import com.jeanbarrossilva.orca.platform.theme.extensions.of

/** [CompositionLocal] that provides [Colors]. */
internal val LocalColors = compositionLocalOf { Colors.Unspecified }

/**
 * [Contrast]s and [Color]s that compose the palette to be used by the application.
 *
 * @param activation [Activation] for representing various activation states.
 * @param background [Contrast] for the overall background.
 * @param disabled [Contrast] for components in a disabled state.
 * @param error [Contrast] for error states.
 * @param link [Color] for links.
 * @param placeholder [Color] for components that indicate loading content.
 * @param primary [Contrast] for primary components.
 * @param secondary [Color] for secondary components.
 * @param surface [Contrast] for on-background or nested containers.
 * @param tertiary [Color] for tertiary components.
 */
@Immutable
data class Colors
internal constructor(
  val activation: Activation,
  val background: Contrast,
  val disabled: Contrast,
  val error: Contrast,
  val link: Color,
  val placeholder: Color,
  val primary: Contrast,
  val secondary: Color,
  val surface: Contrast,
  val tertiary: Color
) {
  /**
   * [Contrast]s representing activation states.
   *
   * @param favorite [Contrast] for a "favorited" state.
   * @param reblog [Contrast] for a "reblogged" state.
   */
  @Immutable
  data class Activation internal constructor(val favorite: Color, val reblog: Color) {
    companion object {
      /** [Activation] with [Contrast.Unspecified] values. */
      internal val Unspecified =
        Activation(favorite = Color.Unspecified, reblog = Color.Unspecified)

      /** [Activation] that's provided by default. */
      internal val Default = Activation(favorite = Color(0xFFD32F2F), reblog = Color(0xFF81C784))
    }
  }

  companion object {
    /** [Colors] that are provided by default. */
    internal val default
      @Composable get() = getDefault(LocalContext.current)

    /** [Colors] with unspecified values. */
    val Unspecified =
      Colors(
        Activation.Unspecified,
        background = Contrast.Unspecified,
        disabled = Contrast.Unspecified,
        error = Contrast.Unspecified,
        link = Color.Unspecified,
        placeholder = Color.Unspecified,
        primary = Contrast.Unspecified,
        secondary = Color.Unspecified,
        surface = Contrast.Unspecified,
        tertiary = Color.Unspecified
      )

    /**
     * Gets the [Colors] that are provided by default.
     *
     * @param context [Context] from which the [Color]s will be obtained.
     */
    @JvmStatic
    fun getDefault(context: Context): Colors {
      return Colors(
        Activation.Default,
        Color.of(context, R.color.backgroundContainer) and
          Color.of(context, R.color.backgroundContent),
        Color.of(context, R.color.disabledContainer) and Color.of(context, R.color.disabledContent),
        Color.of(context, R.color.errorContainer) and Color.of(context, R.color.errorContent),
        Color.of(context, R.color.link),
        Color.of(context, R.color.placeholder),
        Color.of(context, R.color.primaryContainer) and Color.of(context, R.color.primaryContent),
        Color.of(context, R.color.secondary),
        Color.of(context, R.color.surfaceContainer) and Color.of(context, R.color.surfaceContent),
        Color.of(context, R.color.tertiary)
      )
    }
  }
}
