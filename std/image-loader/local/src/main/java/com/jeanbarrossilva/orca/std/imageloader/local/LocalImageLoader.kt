package com.jeanbarrossilva.orca.std.imageloader.local

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * [ImageLoader] that loads an [Image] locally through its [resourceID].
 *
 * @param context [Context] through which the [Image] will be obtained.
 * @param resourceID ID of the resource of the [Image].
 */
class LocalImageLoader(private val context: Context, @DrawableRes private val resourceID: Int) :
  ImageLoader {
  override suspend fun load(width: Int, height: Int): Image? {
    return context.getDrawable(resourceID)?.toBitmap(width, height)?.toImage()
  }
}
