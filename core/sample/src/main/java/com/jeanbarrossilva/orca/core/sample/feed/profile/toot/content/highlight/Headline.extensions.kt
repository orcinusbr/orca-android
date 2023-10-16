package com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.core.sample.SampleCoreModule
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image.CoverImageSource
import com.jeanbarrossilva.orca.core.sample.imageLoaderProvider
import com.jeanbarrossilva.orca.std.injector.Injector

/** [Headline] that's returned by [sample]'s getter. */
private val sampleHeadline =
  Headline(
    title = "Mastodon",
    subtitle = "The original server operated by the Mastodon gGmbH non-profit.",
    coverLoader =
      Injector.from<SampleCoreModule>().imageLoaderProvider().provide(CoverImageSource.Default)
  )

/** Sample [Headline]. */
val Headline.Companion.sample
  get() = sampleHeadline
