package com.jeanbarrossilva.orca.std.imageloader.compose

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * [ImageLoader] powered by the Coil library.
 *
 * @param context [Context] through which [ImageRequest]s will be performed.
 **/
internal class CoilImageLoader private constructor(private val context: Context) : ImageLoader {
    override suspend fun load(width: Int, height: Int, url: URL): Image? {
        val request = ImageRequest.Builder(context).data("$url").size(width, height).build()
        return context.imageLoader.execute(request).drawable?.toBitmap()?.toImage()
    }

    companion object {
        /** Single [CoilImageLoader] instance. **/
        @Suppress("StaticFieldLeak")
        private lateinit var instance: CoilImageLoader

        /**
         * Creates or retrieves an existing instance of a [CoilImageLoader].
         *
         * @param context [Context] through which [ImageRequest]s will be performed.
         **/
        fun getInstance(context: Context): CoilImageLoader {
            return if (::instance.isInitialized) {
                instance
            } else {
                instance = CoilImageLoader(context)
                instance
            }
        }
    }
}
