package com.jeanbarrossilva.orca.std.imageloader.compose

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import com.jeanbarrossilva.orca.std.imageloader.Image

/** Converts this [Image] into a [Bitmap]. **/
internal fun Image.toBitmap(): Bitmap {
    return createBitmap(width, height).apply {
        pixels.forEach {
            setPixel(it.x, it.y, it.color)
        }
    }
}
