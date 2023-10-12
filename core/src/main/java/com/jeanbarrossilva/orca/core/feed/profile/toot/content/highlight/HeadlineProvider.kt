package com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight

import java.net.URL

/** Provides a [Headline] through [provide]. */
fun interface HeadlineProvider {
  /**
   * Provides a [Headline] based on the [url].
   *
   * @param url [URL] for which the [Headline] will be provided.
   */
  fun provide(url: URL): Headline?
}
