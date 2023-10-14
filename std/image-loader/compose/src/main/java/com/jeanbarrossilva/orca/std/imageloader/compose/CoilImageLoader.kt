package com.jeanbarrossilva.orca.std.imageloader.compose

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.jeanbarrossilva.orca.std.imageloader.AsyncImageLoader
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.local.toImage
import java.net.URL

/**
 * [ImageLoader] powered by the Coil library.
 *
 * @param context [Context] through which [ImageRequest]s will be performed.
 */
class CoilImageLoader(private val context: Context, override val source: URL) : AsyncImageLoader() {
  override suspend fun load(width: Int, height: Int): Image? {
    val request = ImageRequest.Builder(context).data("$source").size(width, height).build()
    return context.imageLoader.execute(request).drawable?.toBitmap()?.toImage()
  }
}
