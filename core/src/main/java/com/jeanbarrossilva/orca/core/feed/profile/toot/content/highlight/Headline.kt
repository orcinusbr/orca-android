package com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight

import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader

/**
 * Main information of a [Highlight].
 *
 * @param title Title of the external site.
 * @param subtitle Brief description of the content in the external site.
 * @param coverLoader [ImageLoader] that loads the image that represents the content.
 */
data class Headline(val title: String, val subtitle: String?, val coverLoader: SomeImageLoader?) {
  companion object
}
