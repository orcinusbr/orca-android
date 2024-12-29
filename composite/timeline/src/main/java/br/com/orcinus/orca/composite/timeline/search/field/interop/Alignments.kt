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

package br.com.orcinus.orca.composite.timeline.search.field.interop

import android.view.Gravity
import android.view.View
import androidx.annotation.GravityInt
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection

/**
 * Converts this [Alignment] into a [Gravity] flag.
 *
 * @param size Dimensions of the [View] to be aligned.
 * @param space Size of the parent of the [View].
 * @param layoutDirection Direction of the layout, required for calculating the aligned position.
 * @throws UnsupportedOperationException If this is not a predefined bi-dimensional [Alignment].
 */
@GravityInt
@Throws(UnsupportedOperationException::class)
internal fun Alignment.asGravity(
  size: IntSize,
  space: IntSize,
  layoutDirection: LayoutDirection
): Int {
  // Fast-returns if this alignment is referentially equal to a predefined one…
  when {
    this === Alignment.TopStart -> return Gravity.START or Gravity.TOP
    this === Alignment.TopCenter -> return Gravity.CENTER_HORIZONTAL or Gravity.TOP
    this === Alignment.TopEnd -> return Gravity.TOP or Gravity.END
    this === Alignment.CenterStart -> return Gravity.START or Gravity.CENTER_VERTICAL
    this === Alignment.Center -> return Gravity.CENTER
    this === Alignment.CenterEnd -> return Gravity.CENTER_VERTICAL or Gravity.END
    this === Alignment.BottomEnd -> return Gravity.END or Gravity.BOTTOM
    this === Alignment.BottomCenter -> return Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
    this === Alignment.BottomStart -> return Gravity.START or Gravity.BOTTOM
  }

  /*
   * …otherwise, it is custom, and may or may not be behaviorally equal to a predefined one (when
   * different, an exception is later thrown); thus, the flag is attempted to be determined based on
   * the aligned position.
   */
  val (alignedX, alignedY) = align(size, space, layoutDirection)
  var gravity = Gravity.NO_GRAVITY
  when (alignedX) {
    0 -> gravity = Gravity.START
    space.width / 2 - size.width / 2 -> gravity = Gravity.CENTER_HORIZONTAL
    space.width - size.width -> gravity = Gravity.END
  }
  when (alignedY) {
    0 -> gravity = gravity or Gravity.TOP
    space.height / 2 - size.height / 2 -> gravity = gravity or Gravity.CENTER_VERTICAL
    space.height - size.height -> gravity = gravity or Gravity.BOTTOM
  }

  require(Gravity.isHorizontal(gravity) && Gravity.isVertical(gravity)) {
    "Conversion of a non-predefined bi-dimensional alignment (non-top-start, -top-center, " +
      "-top-end, -center-start, -center, -center-end, -bottom-end, -bottom-center or " +
      "-bottom-start) into a gravity flag is unsupported."
  }
  return gravity
}
