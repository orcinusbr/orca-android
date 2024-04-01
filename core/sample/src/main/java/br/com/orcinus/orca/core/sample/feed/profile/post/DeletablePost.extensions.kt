/*
 * Copyright © 2023-2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.sample.feed.profile.post

import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.DeletablePost
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.sample.feed.profile.post.content.highlight.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.content.sample
import br.com.orcinus.orca.core.sample.feed.profile.post.repost.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.stat.createSampleToggleableStat
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
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
 */
context(Posts.Builder.AdditionScope)

fun DeletablePost.Companion.createSample(
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): DeletablePost {
  return SampleDeletablePost(
    SamplePost(
      sampleDeletablePostID,
      Author.createSample(imageLoaderProvider),
      Content.sample,
      publicationDateTime = ZonedDateTime.of(2_003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3")),
      favorite = createSampleToggleableStat(imageLoaderProvider),
      repost = createSampleToggleableStat(imageLoaderProvider),
      URL("https://mastodon.social/@christianselig/110492858891694580"),
      writerProvider
    )
  )
}
