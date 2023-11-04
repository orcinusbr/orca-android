package com.jeanbarrossilva.orca.core.sample.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.reblog.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** ID of a [Toot] created by [createSample]. */
private val sampleTootID = UUID.randomUUID().toString()

/**
 * Creates sample [Toot]s.
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun Toot.Companion.createSamples(
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): List<Toot> {
  return listOf(Reblog.createSample(imageLoaderProvider), createSample(imageLoaderProvider))
}

/**
 * Creates a sample [Toot].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun Toot.Companion.createSample(
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Toot {
  return SampleToot(
    sampleTootID,
    Author.createSample(imageLoaderProvider),
    Content.createSample(imageLoaderProvider),
    publicationDateTime = ZonedDateTime.of(2_003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3")),
    URL("https://mastodon.social/@christianselig/110492858891694580")
  )
}
