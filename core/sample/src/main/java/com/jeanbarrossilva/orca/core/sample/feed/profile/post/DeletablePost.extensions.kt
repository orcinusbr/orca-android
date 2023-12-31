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
import com.jeanbarrossilva.orca.core.feed.profile.post.DeletablePost
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.repost.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** ID of a [DeletablePost] created by [createSample]. */
internal val sampleDeletablePostID = UUID.randomUUID().toString()

/**
 * Creates a sample [DeletablePost].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [SampleImageSource].
 * @param writerProvider [SamplePostWriter.Provider] by which a [SamplePostWriter] for creating
 *   [SampleDeletablePost]s from [SamplePost]s can be provided.
 */
fun DeletablePost.Companion.createSample(
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>,
  writerProvider: SamplePostWriter.Provider
): DeletablePost {
  return SampleDeletablePost(
    SamplePost(
      sampleDeletablePostID,
      Author.createSample(imageLoaderProvider),
      Content.sample,
      publicationDateTime = ZonedDateTime.of(2_003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3")),
      URL("https://mastodon.social/@christianselig/110492858891694580"),
      writerProvider
    )
  )
}
