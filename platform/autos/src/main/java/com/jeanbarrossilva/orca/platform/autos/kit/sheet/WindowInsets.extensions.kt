/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.autos.kit.sheet

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

/** [WindowInsets] returned by [WindowInsets.Companion.Zero]. */
@Stable private val zeroWindowInsets = WindowInsets(left = 0, top = 0, right = 0, bottom = 0)

/** [ProvidableCompositionLocal] that provides [WindowInsets]. */
internal val LocalWindowInsets = compositionLocalOf {
  WindowInsets(
    left = Dp.Unspecified,
    top = Dp.Unspecified,
    right = Dp.Unspecified,
    bottom = Dp.Unspecified
  )
}

/** [WindowInsets] with zeroed bounds. */
internal val WindowInsets.Companion.Zero
  @Stable get() = zeroWindowInsets

/**
 * Returns either this [WindowInsets] or the result of [fallback] if this one has one unspecified
 * bound.
 *
 * @param fallback Creates the [WindowInsets] to be provided if the receiver is unspecified.
 * @see Dp.Unspecified
 */
@Composable
internal inline fun WindowInsets.takeOrElse(fallback: () -> WindowInsets): WindowInsets {
  val density = LocalDensity.current
  val layoutDirection = LocalLayoutDirection.current
  return try {
    getLeft(density, layoutDirection)
    getTop(density)
    getRight(density, layoutDirection)
    getBottom(density)
    this
  } catch (_: IllegalArgumentException) {
    fallback()
  }
}
