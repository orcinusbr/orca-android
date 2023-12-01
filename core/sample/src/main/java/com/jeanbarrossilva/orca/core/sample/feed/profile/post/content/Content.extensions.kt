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

package com.jeanbarrossilva.orca.core.sample.feed.profile.post.content

import com.jeanbarrossilva.orca.core.feed.profile.post.content.Attachment
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.styledstring.buildStyledString
import java.net.URL

/**
 * Creates a sample [Content].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun Content.Companion.createSample(
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Content {
  val highlight = Highlight.createSample(imageLoaderProvider)
  return from(
    Domain.sample,
    buildStyledString {
      +"This is a "
      bold { +"sample" }
      +" "
      italic { +"post" }
      +" that has the sole purpose of allowing one to see how it would look like in Orca."
      +"\n".repeat(2)
      +highlight.url.toString()
    },
    attachments =
      listOf(
        Attachment(
          description = "Abstract art",
          URL("https://images.unsplash.com/photo-1692890846581-da1a95435f34")
        )
      )
  ) {
    highlight.headline
  }
}
