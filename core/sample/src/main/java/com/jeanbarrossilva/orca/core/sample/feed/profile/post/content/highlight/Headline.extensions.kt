package com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.sample.image.CoverImageSource
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a sample [Headline].
 *
 * @param coverLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Headline]'s cover will be loaded.
 */
fun Headline.Companion.createSample(
  coverLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Headline {
  return Headline(
    title = "Mastodon",
    subtitle = "The original server operated by the Mastodon gGmbH non-profit.",
    coverLoaderProvider.provide(CoverImageSource.Default)
  )
}
