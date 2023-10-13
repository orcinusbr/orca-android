package com.jeanbarrossilva.orca.std.imageloader.test

import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.buildImage

/** [ImageLoader] that loads an empty [Image]. */
object TestImageLoader : ImageLoader {
  override suspend fun load(width: Int, height: Int): Image {
    return buildImage(width = 1, height = 1) { pixel(0) }
  }
}
