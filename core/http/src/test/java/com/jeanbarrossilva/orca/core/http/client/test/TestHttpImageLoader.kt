package com.jeanbarrossilva.orca.core.http.client.test

import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.buildImage
import java.net.URL

/** [ImageLoader] that loads an empty [Image] from a [URL]. */
internal class TestHttpImageLoader private constructor(override val source: URL) :
  ImageLoader<URL> {
  /** [ImageLoader.Provider] that provides a [TestHttpImageLoader]. */
  object Provider : ImageLoader.Provider<URL> {
    override fun provide(source: URL): TestHttpImageLoader {
      return TestHttpImageLoader(source)
    }
  }

  override suspend fun load(width: Int, height: Int): Image {
    return buildImage(width = 1, height = 1) { pixel(0) }
  }
}
