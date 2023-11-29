package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.repost.Repost
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.repost.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** ID of a [Post] created by [createSample]. */
private val samplePostID = UUID.randomUUID().toString()

/**
 * Creates sample [Post]s.
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun Post.Companion.createSamples(
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): List<Post> {
  return listOf(Repost.createSample(imageLoaderProvider), createSample(imageLoaderProvider))
}

/**
 * Creates a sample [Post].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun Post.Companion.createSample(
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Post {
  return SamplePost(
    samplePostID,
    Author.createSample(imageLoaderProvider),
    Content.createSample(imageLoaderProvider),
    publicationDateTime = ZonedDateTime.of(2_003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3")),
    URL("https://mastodon.social/@christianselig/110492858891694580")
  )
}
