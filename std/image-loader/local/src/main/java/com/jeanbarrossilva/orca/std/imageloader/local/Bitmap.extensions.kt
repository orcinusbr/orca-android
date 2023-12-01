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

package com.jeanbarrossilva.orca.std.imageloader.local

import android.graphics.Bitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.buildImage

/** Converts this [Bitmap] into an [Image]. */
suspend fun Bitmap.toImage(): Image {
  return buildImage(width, height) {
    val nonHardwareBitmap =
      if (config == Bitmap.Config.HARDWARE) {
        copy(Bitmap.Config.ARGB_8888, false)
      } else {
        this@toImage
      }
    IntArray(size = width * height)
      .also { nonHardwareBitmap.getPixels(it, 0, width, 0, 0, width, height) }
      .forEach(::pixel)
  }
}
