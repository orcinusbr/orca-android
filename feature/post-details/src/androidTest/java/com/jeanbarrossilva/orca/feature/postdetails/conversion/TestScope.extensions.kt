/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.feature.postdetails.conversion

import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.GalleryPreview
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import com.jeanbarrossilva.orca.composite.timeline.post.figure.link.LinkCard
import com.jeanbarrossilva.orca.composite.timeline.stat.details.StatsDetails
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.PostProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.feature.postdetails.PostDetails
import com.jeanbarrossilva.orca.feature.postdetails.toPostDetails
import com.jeanbarrossilva.orca.feature.postdetails.toPostDetailsFlow
import com.jeanbarrossilva.orca.platform.core.sample
import com.jeanbarrossilva.orca.platform.core.withSample
import com.jeanbarrossilva.orca.platform.core.withSamples
import com.jeanbarrossilva.orca.std.image.ImageLoader
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/**
 * [CoroutineScope] created from the [TestScope] in which the test is running, that houses
 * [Post]-to-[PostDetails]-conversion-specific functionality.
 *
 * @param delegate [TestScope] to which [CoroutineScope]-like behavior is delegated.
 * @param postID ID of the [Post] that serves as the base for all underlying operations.
 * @see Post.id
 */
internal class PostDetailsConversionScope(delegate: TestScope, private val postID: String) :
  CoroutineScope by delegate {
  /** [Detailing] to be returned by [detailed]. */
  private val detailing by lazy(::Detailing)

  /**
   * Authenticated [Actor] whose avatar [ImageLoader] will be used to load the image.
   *
   * @see Actor.Authenticated
   * @see Actor.Authenticated.avatarLoader
   */
  val actor = Actor.Authenticated.sample

  /**
   * [Colors] by which the text of a [PostDetails] can be colored.
   *
   * @see PostDetails.text
   */
  val colors = Colors.LIGHT

  /**
   * Callback for when the [LinkCard] of [PostDetails] is clicked that's used in the conversion
   * process of the [post].
   */
  val onLinkClick = { _: URL -> }

  /**
   * [Disposition.OnThumbnailClickListener] that listens to clicks on [PostDetails]'
   * [GalleryPreview]'s thumbnails.
   */
  val onThumbnailClickListener = Disposition.OnThumbnailClickListener.empty

  /**
   * [Post] to be added as a comment.
   *
   * @see comment
   */
  val comment = Posts.withSamples.last()

  /**
   * Offers multiple ways to obtain [PostDetails] from the given [post].
   *
   * @see withCommentCountOf
   * @see favorited
   * @see unfavorited
   * @see reposted
   * @see unreposted
   * @see asFlow
   */
  inner class Detailing {
    /**
     * Converts the [post] into [PostDetails] that have an exact amount of comments.
     *
     * @param commentCount Quantity of comments for the returned [PostDetails] to have.
     */
    suspend fun withCommentCountOf(commentCount: Int): PostDetails {
      return withStats { copy(commentCount = commentCount) }
    }

    /** Converts the [post] into favorited [PostDetails]. */
    suspend fun favorited(): PostDetails {
      return withStats { copy(isFavorite = true) }
    }

    /** Converts the [post] into unfavorited [PostDetails]. */
    suspend fun unfavorited(): PostDetails {
      return withStats { copy(isFavorite = false) }
    }

    /** Converts the [post] into reposted [PostDetails]. */
    suspend fun reposted(): PostDetails {
      return withStats { copy(isReposted = true) }
    }

    /** Converts the [post] into unreposted [PostDetails]. */
    suspend fun unreposted(): PostDetails {
      return withStats { copy(isReposted = false) }
    }

    /**
     * Converts the [post] into a [Flow] of [PostDetails], to which emissions are performed whenever
     * one of its [Stat]s is updated.
     */
    suspend fun asFlow(): Flow<PostDetails> {
      return post().toPostDetailsFlow(colors, onLinkClick, onThumbnailClickListener)
    }

    /**
     * Converts the [post] into [PostDetails] to whose [StatsDetails] the [update] is applied.
     *
     * @param update Updates the current [StatsDetails] of the [PostDetails] to which the [post] has
     *   been converted.
     * @see PostDetails.stats
     */
    private suspend fun withStats(update: StatsDetails.() -> StatsDetails): PostDetails {
      val details = post().toPostDetails(colors, onLinkClick, onThumbnailClickListener)
      val stats = details.stats.update()
      return details.copy(stats = stats)
    }
  }

  /**
   * Obtains the [Post] identified as [postID] provided by the sample [Instance]'s [PostProvider].
   *
   * @see Instance.Companion.sample
   * @see PostProvider
   */
  suspend fun post(): Post {
    return Instance.sample.postProvider.provide(postID).first()
  }

  /**
   * Obtains a [Detailing] through which [PostDetails] derived from the [post] can be obtained in
   * various ways (i. e., as a single instance with a specific amount of comments or as a [Flow]).
   *
   * @see Detailing.withCommentCountOf
   * @see Detailing.asFlow
   */
  fun detailed(): Detailing {
    return detailing
  }

  /**
   * Adds a comment to the [post].
   *
   * @see uncomment
   */
  suspend fun comment() {
    post().comment.add(comment)
  }

  /**
   * Removes the sample comment added to the [post].
   *
   * @see comment
   */
  suspend fun uncomment() {
    post().comment.remove(comment)
  }

  /**
   * Favorites the [post].
   *
   * @see unfavorite
   */
  suspend fun favorite() {
    post().favorite.enable()
  }

  /**
   * Unfavorites the [post].
   *
   * @see favorite
   */
  suspend fun unfavorite() {
    post().favorite.disable()
  }

  /**
   * Reposts the [post].
   *
   * @see unrepost
   */
  suspend fun repost() {
    post().repost.enable()
  }

  /**
   * Unreposts the [post].
   *
   * @see repost
   */
  suspend fun unrepost() {
    post().repost.disable()
  }
}

/**
 * Creates an environment for testing [Post]-to-[PostDetails] conversion.
 *
 * @param postID ID of the [Post] that serves as the base for all underlying operations. Defaults to
 *   that of the sample one.
 * @param body Testing to be performed.
 * @see Post.id
 * @see Posts.Companion.withSample
 */
internal fun runPostDetailsConversionTest(
  postID: String = Posts.withSample.single().id,
  body: suspend PostDetailsConversionScope.() -> Unit
) {
  runTest { PostDetailsConversionScope(delegate = this, postID).body() }
}
