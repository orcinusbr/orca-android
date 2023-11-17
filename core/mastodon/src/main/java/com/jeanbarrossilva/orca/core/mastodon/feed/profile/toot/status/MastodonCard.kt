package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that represents the most prominent content of an [MastodonStatus].
 *
 * @param url URL [String] that leads to the webpage to which this [MastodonCard] refers.
 * @param title Title of the webpage.
 * @param description Description of the webpage.
 * @param image URL [String] that leads to the cover image.
 */
@Serializable
internal data class MastodonCard(
  val url: String,
  val title: String,
  val description: String,
  val image: String?
) {
  /**
   * Converts this [MastodonCard] into a [Headline].
   *
   * @param coverLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   cover will be loaded from a [URL].
   * @return Resulting [Headline] or `null` if the [image] is unavailable.
   */
  fun toHeadline(coverLoaderProvider: ImageLoader.Provider<URL>): Headline? {
    return image?.let {
      Headline(
        title,
        subtitle = description.ifEmpty { null },
        coverLoader = coverLoaderProvider.provide(URL(image))
      )
    }
  }
}
