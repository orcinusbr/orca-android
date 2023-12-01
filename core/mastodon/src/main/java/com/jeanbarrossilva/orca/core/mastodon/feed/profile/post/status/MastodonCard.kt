/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
