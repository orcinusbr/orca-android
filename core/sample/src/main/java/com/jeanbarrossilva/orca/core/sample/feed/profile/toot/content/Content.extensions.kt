package com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Attachment
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
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
    buildStyledString {
      +"This is a "
      bold { +"sample" }
      italic { +"toot" }
      +"that has the sole purpose of allowing one to see how it would look like in Orca."
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
