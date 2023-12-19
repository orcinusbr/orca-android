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
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.feed.profile.post.repost.Repost
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.repost.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import com.jeanbarrossilva.orca.std.styledstring.buildStyledString
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** ID of a [Post] created by [createSample]. */
private val samplePostID = UUID.randomUUID().toString()

/** ID of the third [Post] in the [List] returned by [createSamples]. */
private val thirdPostID = UUID.randomUUID().toString()

/**
 * Creates sample [Post]s.
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [SampleImageSource].
 */
fun Post.Companion.createSamples(
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): List<Post> {
  return listOf(
    Repost.createSample(imageLoaderProvider),
    createSample(imageLoaderProvider),
    SamplePost(
      thirdPostID,
      Author.createChristianSample(imageLoaderProvider),
      Content.from(
        Domain.sample,
        text =
          buildStyledString {
            +("Also, last day to get Pixel Pals premium at a discount and last day for the " +
              "lifetime unlock to be available!")
            +"\n".repeat(2)
            +Highlight.createSample(imageLoaderProvider).url.toString()
          }
      ) {
        Headline.createSample(imageLoaderProvider)
      },
      publicationDateTime =
        ZonedDateTime.of(2_023, 11, 27, 18, 26, 0, 0, ZoneId.of("America/Halifax")),
      URL("https://mastodon.social/@christianselig/111484624066823391")
    )
  )
}

/**
 * Creates a sample [Post].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [SampleImageSource].
 */
fun Post.Companion.createSample(
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): Post {
  return SamplePost(
    samplePostID,
    Author.createSample(imageLoaderProvider),
    Content.sample,
    publicationDateTime = ZonedDateTime.of(2_003, 10, 8, 8, 0, 0, 0, ZoneId.of("GMT-3")),
    URL("https://mastodon.social/@christianselig/110492858891694580")
  )
}
