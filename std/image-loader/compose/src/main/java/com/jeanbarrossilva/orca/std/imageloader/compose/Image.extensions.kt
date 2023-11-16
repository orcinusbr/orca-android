package com.jeanbarrossilva.orca.std.imageloader.compose

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/** Converts this [Image] into a [Bitmap]. */
internal suspend fun Image.toBitmap(): Bitmap {
  return createBitmap(width, height).apply {
    withContext(Dispatchers.IO) {
      coroutineScope { pixels.forEach { setPixel(it.x, it.y, it.color) } }
    }
  }
}
