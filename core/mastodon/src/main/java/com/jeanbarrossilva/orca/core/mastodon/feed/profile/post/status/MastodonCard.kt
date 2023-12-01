/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.status

import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
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
