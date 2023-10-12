package com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import java.net.URL

/** [Headline] that's returned by [sample]'s getter. */
private val sampleHeadline =
  Headline(
    title = "Mastodon",
    subtitle = "The original server operated by the Mastodon gGmbH non-profit.",
    coverURL =
      URL("https://files.mastodon.social/site_uploads/files/000/000/001/@1x/57c12f441d083cde.png")
  )

/** Sample [Headline]. */
val Headline.Companion.sample
  get() = sampleHeadline
