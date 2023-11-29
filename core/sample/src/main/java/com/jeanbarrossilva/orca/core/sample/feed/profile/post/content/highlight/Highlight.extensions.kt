package com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * Creates a sample [Highlight].
 *
 * @param coverLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Highlight]'s [headline][Highlight.headline]'s cover will be loaded from [SampleImageSource].
 */
fun Highlight.Companion.createSample(
  coverLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Highlight {
  return Highlight(Headline.createSample(coverLoaderProvider), URL("https://mastodon.social"))
}
