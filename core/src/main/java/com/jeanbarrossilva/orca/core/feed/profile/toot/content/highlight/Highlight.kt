package com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import java.net.URL

/**
 * Main portion of a [Content] that redirects the user to another site.
 *
 * @param headline [Headline] with the main information.
 * @param url [URL] that leads to the external site.
 */
data class Highlight(val headline: Headline, val url: URL) {
  companion object
}
