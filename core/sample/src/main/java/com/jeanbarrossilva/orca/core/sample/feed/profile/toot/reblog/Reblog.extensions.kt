package com.jeanbarrossilva.orca.core.sample.feed.profile.toot.reblog

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleToot
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.createRamboSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** ID of a [Reblog] created by [createSample]. */
private val sampleReblogID = UUID.randomUUID().toString()

/**
 * Creates a sample [Reblog].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun Reblog.Companion.createSample(
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Reblog {
  return Reblog(
    SampleToot(
      sampleReblogID,
      Author.createRamboSample(imageLoaderProvider),
      Content.from(
        Domain.sample,
        StyledString(
          "Programming life hack. Looking for real-world examples of how an API is used? Search " +
            "for code on GitHub like so: “APINameHere path:*.extension”. Practical example for a " +
            "MusicKit API in Swift: “MusicCatalogResourceRequest extension:*.swift”. I can " +
            "usually find lots of examples to help me get things going without having to read " +
            "the entire documentation for a given API."
        )
      ) {
        null
      },
      publicationDateTime = ZonedDateTime.of(2023, 8, 16, 16, 48, 43, 384, ZoneId.of("GMT-3")),
      url = URL("https://mastodon.social/@_inside/110900315644335855")
    ),
    reblogger = Author.createSample(imageLoaderProvider)
  )
}
