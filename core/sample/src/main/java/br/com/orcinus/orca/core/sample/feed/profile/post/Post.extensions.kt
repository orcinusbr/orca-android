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

package br.com.orcinus.orca.core.sample.feed.profile.post

import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.DeletablePost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Headline
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Highlight
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.feed.profile.post.content.highlight.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.repost.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.stat.createSampleToggleableStat
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import br.com.orcinus.orca.std.styledstring.buildStyledString
import java.net.URL
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/** ID of the third [Post] in the [List] returned by [createSamples]. */
private val thirdPostID = UUID.randomUUID().toString()

/**
 * Creates sample [Post]s.
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [SampleImageSource].
 */
context(Posts.Builder.AdditionScope)

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
      favorite = createSampleToggleableStat(imageLoaderProvider),
      repost = createSampleToggleableStat(imageLoaderProvider),
      URL("https://mastodon.social/@christianselig/111484624066823391"),
      writerProvider
    )
  )
}

/**
 * Creates a sample [Post].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [SampleImageSource].
 */
context(Posts.Builder.AdditionScope)

fun Post.Companion.createSample(
  imageLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): Post {
  return DeletablePost.createSample(imageLoaderProvider)
}
