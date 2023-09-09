package com.jeanbarrossilva.orca.std.imageloader.compose

import android.graphics.Bitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.buildImage

/** Converts this [Bitmap] into an [Image]. **/
internal fun Bitmap.toImage(): Image {
    return buildImage(width, height) {
        intArrayOf().also { getPixels(it, 0, 0, 0, 0, width, height) }.forEach(::pixel)
    }
}
