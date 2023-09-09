package com.jeanbarrossilva.orca.std.imageloader.compose

import android.graphics.Bitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.buildImage

/** Converts this [Bitmap] into an [Image]. **/
internal fun Bitmap.toImage(): Image {
    return buildImage(width, height) {
        IntArray(width * height)
            .also {
                copy(Bitmap.Config.ARGB_8888, false).getPixels(it, 0, width, 0, 0, width, height)
            }
            .forEach(::pixel)
    }
}
