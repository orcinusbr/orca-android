package com.jeanbarrossilva.orca.std.imageloader.compose

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/** [ImageLoader] powered by the Coil library. **/
internal class CoilImageLoader(private val context: Context) : ImageLoader {
    override suspend fun load(url: URL): Image? {
        val request = ImageRequest.Builder(context).data("$url").build()
        return context.imageLoader.execute(request).drawable?.toBitmap()?.toImage()
    }
}
