package com.jeanbarrossilva.orca.core.sample.rule

import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.buildImage

/** [ImageLoader] that loads an empty [Image] from a [SampleImageSource]. */
internal object TestSampleImageLoader : ImageLoader<SampleImageSource> {
  override val source = SampleImageSource.None

  /** [ImageLoader.Provider] that provides a [TestSampleImageLoader]. */
  object Provider : ImageLoader.Provider<SampleImageSource> {
    override fun provide(source: SampleImageSource): TestSampleImageLoader {
      return TestSampleImageLoader
    }
  }

  override suspend fun load(width: Int, height: Int): Image {
    return buildImage(width = 1, height = 1) { pixel(0) }
  }
}
