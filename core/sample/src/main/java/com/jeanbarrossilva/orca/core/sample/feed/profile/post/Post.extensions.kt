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
