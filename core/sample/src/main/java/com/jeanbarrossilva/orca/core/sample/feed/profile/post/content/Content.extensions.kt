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
