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
import androidx.core.graphics.createBitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/** Converts this [Image] into a [Bitmap]. */
suspend fun Image.toBitmap(): Bitmap {
  return createBitmap(width, height).apply {
    withContext(Dispatchers.IO) {
      coroutineScope { pixels.forEach { setPixel(it.x, it.y, it.color) } }
    }
  }
}
