/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.std.imageloader.local.drawable

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.buildImage
import com.jeanbarrossilva.orca.std.imageloader.local.isExplicit

/**
 * Converts this [Drawable] into an [Image].
 *
 * @param size Size in which the [Image] will be sized, defaulting to this [Drawable]'s intrinsic
 *   dimensions if the [size]'s are automatic.
 * @throws IllegalArgumentException If the both of the [size]'s dimensions are automatic (which
 *   shouldn't be possible).
 * @see Drawable.getIntrinsicWidth
 * @see Drawable.getIntrinsicHeight
 * @see ImageLoader.Size.Dimension
 * @see ImageLoader.Size.Dimension.Automatic
 */
@Throws(IllegalArgumentException::class)
suspend fun Drawable.toImage(size: ImageLoader.Size): Image {
  val explicit = explicit(size)
  val width = explicit.first.value
  val height = explicit.second.value
  val bitmap =
    toBitmap(width, height).let {
      if (it.config == Bitmap.Config.HARDWARE) it.copy(Bitmap.Config.ARGB_8888, false) else it
    }
  return buildImage(width, height) {
    IntArray(width * height)
      .also { bitmap.getPixels(it, 0, width, 0, 0, width, height) }
      .forEach(::pixel)
  }
}

/**
 * Explicits the dimension that's been defined to be calculated automatically by scaling it to the
 * value of the other one.
 *
 * @return [Pair] with both explicit dimensions.
 * @throws IllegalArgumentException If, for some unknown, evil, Machiavellian reason, both of the
 *   [size]'s dimensions are automatic.
 * @see ImageLoader.Size.Dimension.Explicit
 * @see ImageLoader.Size.Dimension.Automatic
 * @see ImageLoader.Size.Dimension.Explicit.value
 * @see scale
 */
@Throws(IllegalArgumentException::class)
private fun Drawable.explicit(
  size: ImageLoader.Size
): Pair<ImageLoader.Size.Dimension.Explicit, ImageLoader.Size.Dimension.Explicit> {
  val (width, height) = size
  val scale = { ab: Int, pb: Int, ps: Int -> ImageLoader.Size.Dimension.Explicit(ab.scale(pb, ps)) }
  return when {
    width.isExplicit() -> width to scale(intrinsicHeight, intrinsicWidth, width.value)
    height.isExplicit() -> scale(intrinsicWidth, intrinsicHeight, height.value) to height
    else -> throw IllegalArgumentException("Size shouldn't have both dimensions being automatic.")
  }
}
