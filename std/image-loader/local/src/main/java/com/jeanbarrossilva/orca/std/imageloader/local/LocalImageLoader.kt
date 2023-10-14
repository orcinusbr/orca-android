package com.jeanbarrossilva.orca.std.imageloader.local

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * [ImageLoader] that loads an [Image] locally through its resource ID.
 *
 * @param context [Context] through which the underlying [Drawable] will be obtained.
 */
class LocalImageLoader(private val context: Context, @DrawableRes override val source: Int) :
  ImageLoader<Int> {
  override suspend fun load(width: Int, height: Int): Image? {
    return context.getDrawable(source)?.toBitmap(width, height)?.toImage()
  }
}
