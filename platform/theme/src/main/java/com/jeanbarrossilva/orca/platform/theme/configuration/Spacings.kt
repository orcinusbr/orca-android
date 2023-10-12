package com.jeanbarrossilva.orca.platform.theme.configuration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** [CompositionLocal] that provides [Spacings]. */
internal val LocalSpacings = compositionLocalOf { Spacings.Unspecified }

/**
 * Sizes for spacing, such as the distance between one component and another or padding.
 *
 * @param extraLarge Greatest possible size.
 * @param large Smaller than [extraLarge], greater than [medium].
 * @param medium Smaller than [large], greater than [small].
 * @param small Smaller than [medium], greater than [extraSmall].
 * @param extraSmall Smallest possible size.
 */
data class Spacings
internal constructor(
  val extraLarge: Dp,
  val large: Dp,
  val medium: Dp,
  val small: Dp,
  val extraSmall: Dp
) {
  companion object {
    /** [Spacings] with [Dp.Unspecified] values. */
    internal val Unspecified =
      Spacings(
        extraLarge = Dp.Unspecified,
        large = Dp.Unspecified,
        medium = Dp.Unspecified,
        small = Dp.Unspecified,
        extraSmall = Dp.Unspecified
      )

    /** [Spacings] that are provided by default. */
    val default
      @Composable
      get() =
        Spacings(extraLarge = 32.dp, large = 24.dp, medium = 16.dp, small = 8.dp, extraSmall = 4.dp)
  }
}
