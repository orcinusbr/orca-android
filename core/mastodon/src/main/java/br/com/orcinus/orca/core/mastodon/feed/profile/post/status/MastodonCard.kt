/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.feed.profile.post.status

import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Headline
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that represents the most prominent content of A [MastodonStatus].
 *
 * @param uri URI [String] that leads to the webpage to which this [MastodonCard] refers.
 * @param title Title of the webpage.
 * @param description Description of the webpage.
 * @param image URI [String] that leads to the cover image.
 */
@Serializable
internal data class MastodonCard(
  val uri: String,
  val title: String,
  val description: String,
  val image: String?
) {
  /**
   * Converts this [MastodonCard] into a [Headline].
   *
   * @param coverLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   cover will be loaded from a [URI].
   * @return Resulting [Headline] or `null` if the image is unavailable.
   */
  fun toHeadline(coverLoaderProvider: SomeImageLoaderProvider<URI>): Headline? {
    return image?.let {
      Headline(
        title,
        subtitle = description.ifEmpty { null },
        coverLoader = coverLoaderProvider.provide(URI(image))
      )
    }
  }
}
