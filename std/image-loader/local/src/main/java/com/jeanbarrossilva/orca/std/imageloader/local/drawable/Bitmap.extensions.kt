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
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

/**
 * Samples this [Bitmap] to the given [sampledWidth] and [sampledHeight]. Usually prevents
 * high-demanding memory operations performed on this [Bitmap] (such as reading its pixels) from
 * throwing an [OutOfMemoryError].
 *
 * If the given dimensions are the same as this [Bitmap]'s, then no sampling is performed whatsoever
 * and this unmodified [Bitmap] is returned.
 *
 * @param sampledWidth Width according to which this [Bitmap] will be sampled.
 * @param sampledHeight Height according to which this [Bitmap] will be sampled.
 * @see Bitmap.getPixels
 */
internal fun Bitmap.sample(sampledWidth: Int, sampledHeight: Int): Bitmap {
  return if (width != sampledWidth && height != sampledHeight) {
    sampleDisregardingDimensionEquality(sampledWidth, sampledHeight)
  } else {
    this
  }
}

/** Converts this [Bitmap] into a [ByteArray]. */
internal fun Bitmap.toByteArray(): ByteArray {
  return ByteArrayOutputStream()
    .use { it.apply { compress(Bitmap.CompressFormat.PNG, 100, it) } }
    .toByteArray()
}

/**
 * Returns a copy of this [Bitmap] with [Bitmap.Config.ARGB_8888] as its configuration if it
 * currently is [Bitmap.Config.HARDWARE]; otherwise, just returns this [Bitmap].
 *
 * @see Bitmap.copy
 * @see Bitmap.getConfig
 */
internal fun Bitmap.withoutHardwareConfiguration(): Bitmap {
  return if (config == Bitmap.Config.HARDWARE) copy(Bitmap.Config.ARGB_8888, false) else this
}

/**
 * Samples this [Bitmap] to the given [sampledWidth] and [sampledHeight]. Usually prevents
 * high-demanding memory operations performed on this [Bitmap] (such as reading its pixels) from
 * throwing an [OutOfMemoryError].
 *
 * As the name of this method suggests, a new [Bitmap] will be created even if this one's and the
 * given dimensions are the same.
 *
 * @param sampledWidth Width according to which this [Bitmap] will be sampled.
 * @param sampledHeight Height according to which this [Bitmap] will be sampled.
 * @see Bitmap.getPixels
 */
private fun Bitmap.sampleDisregardingDimensionEquality(
  sampledWidth: Int,
  sampledHeight: Int
): Bitmap {
  val byteArray = toByteArray()
  val options =
    BitmapFactory.Options().apply {
      inBitmap = this@sampleDisregardingDimensionEquality
      inSampleSize = 1
      if (width > sampledWidth || height > sampledHeight) {
        while (
          width / 1.5f / inSampleSize > sampledWidth || height / 1.5f / inSampleSize > sampledHeight
        ) {
          inSampleSize *= 2
        }
      }
    }
  return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
}
