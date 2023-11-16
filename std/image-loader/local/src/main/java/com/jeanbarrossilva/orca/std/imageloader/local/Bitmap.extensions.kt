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
