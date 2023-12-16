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

package com.jeanbarrossilva.orca.std.image.local.drawable

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.jeanbarrossilva.orca.std.image.Image
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.buildImage

/**
 * Converts this [Drawable] into an [Image].
 *
 * @param size Size according to which the [Image] will be sized.
 */
suspend fun Drawable.toImage(size: ImageLoader.Size): Image {
  require(this is BitmapDrawable && bitmap != null) {
    "A drawable needs to be a BitmapDrawable and have an actual bitmap for it to be converted " +
      "into an image."
  }
  val scaledWidth = size.width.value() ?: intrinsicWidth
  val scaledHeight = size.height.value() ?: ((intrinsicHeight * scaledWidth) / intrinsicWidth)
  val scaledBitmap = bitmap.withoutHardwareConfiguration().sample(scaledWidth, scaledHeight)
  return with(scaledBitmap) {
    buildImage(width, height) {
      IntArray(size = width * height)
        .also { getPixels(it, 0, width, 0, 0, width, height) }
        .forEach(::pixel)
        .also { scaledBitmap.recycle() }
    }
  }
}
