package com.jeanbarrossilva.orca.app.module.core

import android.content.Context
import com.jeanbarrossilva.orca.std.imageloader.AsyncImageLoader
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.CoilImageLoader
import java.net.URL

internal class AsyncImageLoaderProvider(private val context: Context) : ImageLoader.Provider<URL> {
  override fun provide(source: URL): AsyncImageLoader {
    return CoilImageLoader(context, source)
  }
}
