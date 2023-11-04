package com.jeanbarrossilva.orca.core.sample.test.image

import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.buildImage

/** [ImageLoader] that loads an empty [Image] from a [SampleImageSource]. */
class TestSampleImageLoader private constructor() : ImageLoader<SampleImageSource> {
  override val source = SampleImageSource.None

  /** [ImageLoader.Provider] that provides a [TestSampleImageLoader]. */
  object Provider : ImageLoader.Provider<SampleImageSource> {
    override fun provide(source: SampleImageSource): TestSampleImageLoader {
      return instance
    }
  }

  override suspend fun load(width: Int, height: Int): Image {
    return buildImage(width = 1, height = 1) { pixel(0) }
  }

  companion object {
    /** Single instance of a [TestSampleImageLoader]. */
    private val instance = TestSampleImageLoader()
  }
}
