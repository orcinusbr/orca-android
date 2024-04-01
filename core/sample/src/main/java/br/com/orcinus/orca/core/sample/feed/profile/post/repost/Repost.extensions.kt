/*
 * Copyright © 2023-2024 Orca
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

package br.com.orcinus.orca.core.sample.feed.profile.post.repost

import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePost
import br.com.orcinus.orca.core.sample.feed.profile.post.createRamboSample
import br.com.orcinus.orca.core.sample.feed.profile.post.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.stat.createSampleToggleableStat
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.styledstring.StyledString
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** ID of a [Repost] created by [createSample]. */
private val sampleRepostID = UUID.randomUUID().toString()

/**
 * Creates a sample [Repost].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [SampleImageSource].
 */
context(Posts.Builder.AdditionScope)

fun Repost.Companion.createSample(
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): Repost {
  return Repost(
    SamplePost(
      sampleRepostID,
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
      favorite = createSampleToggleableStat(imageLoaderProvider),
      repost = createSampleToggleableStat(imageLoaderProvider),
      url = URL("https://mastodon.social/@_inside/110900315644335855"),
      writerProvider
    ),
    reblogger = Author.createSample(imageLoaderProvider)
  )
}
