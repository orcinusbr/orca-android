/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.feature.postdetails.conversion

import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.GalleryPreview
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import br.com.orcinus.orca.composite.timeline.post.figure.link.LinkCard
import br.com.orcinus.orca.composite.timeline.stat.details.StatsDetails
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.FeedProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.ProfileProvider
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.PostProvider
import br.com.orcinus.orca.core.feed.profile.post.content.TermMuter
import br.com.orcinus.orca.core.feed.profile.post.stat.Stat
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearcher
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticationLock
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticator
import br.com.orcinus.orca.core.sample.auth.SampleAuthorizer
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider
import br.com.orcinus.orca.core.sample.auth.actor.createSample
import br.com.orcinus.orca.core.sample.feed.SampleFeedProvider
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.sample.feed.profile.search.SampleProfileSearcher
import br.com.orcinus.orca.core.sample.image.AuthorImageSource
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.feature.postdetails.PostDetails
import br.com.orcinus.orca.feature.postdetails.toPostDetails
import br.com.orcinus.orca.feature.postdetails.toPostDetailsFlow
import br.com.orcinus.orca.platform.core.image.createSample
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.android.AndroidImageLoader
import java.net.URI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/**
 * [CoroutineScope] created from the [TestScope] in which the test is running, that houses
 * [Post]-to-[PostDetails]-conversion-specific functionality.
 *
 * @param delegate [TestScope] to which [CoroutineScope]-like behavior is delegated.
 */
internal class PostDetailsConversionScope(delegate: TestScope) : CoroutineScope by delegate {
  /** [Instance]-specific [Authenticator] through which authentication can be done. */
  private val authenticator = SampleAuthenticator()

  /** Provides the [ImageLoader] by which images are to be loaded from a [SampleImageSource]. */
  private val imageLoaderProvider = AndroidImageLoader.Provider.createSample(context)

  /**
   * Authenticated [Actor] whose avatar [ImageLoader] will be used to load the image.
   *
   * @see Actor.Authenticated
   * @see Actor.Authenticated.avatarLoader
   */
  val actor =
    Actor.Authenticated.createSample(
      avatarLoader = imageLoaderProvider.provide(AuthorImageSource.Default)
    )

  /** [ActorProvider] whose provided [Actor] will be ensured to be authenticated. */
  private val actorProvider = SampleActorProvider(actor)

  /**
   * [Instance]-specific [AuthenticationLock] by which features can be locked or unlocked
   * authentication "wall".
   */
  private val authenticationLock =
    SampleAuthenticationLock(SampleAuthorizer, authenticator, actorProvider)

  /** [Instance]-specific [ProfileProvider] for providing [Profile]s. */
  private val profileProvider = SampleProfileProvider()

  /** [Instance]-specific [ProfileSearcher] by which search for [Profile]s can be made. */
  private val profileSearcher = SampleProfileSearcher(profileProvider)

  /** [TermMuter] by which [Post]s with muted terms will be filtered out. */
  private val termMuter = SampleTermMuter()

  /** [Instance]-specific [PostProvider] that provides [Post]s. */
  val postProvider = SamplePostProvider(profileProvider)

  /** [Instance]-specific [FeedProvider] that provides the [Post]s in the timeline. */
  private val feedProvider =
    SampleFeedProvider(profileProvider, postProvider, termMuter, imageLoaderProvider)

  /** [Detailing] to be returned by [detailed]. */
  private val detailing by lazy(::Detailing)

  /**
   * [Post] to be added as a comment.
   *
   * @see comment
   */
  private inline val comment
    get() = postProvider.provideAllCurrent().first { it.author.id != actor.id }

  /** A sample [Post]. */
  val post
    get() = postProvider.provideOneCurrent()

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
  val onLinkClick = { _: URI -> }

  /**
   * [Disposition.OnThumbnailClickListener] that listens to clicks on [PostDetails]'
   * [GalleryPreview]'s thumbnails.
   */
  val onThumbnailClickListener = Disposition.OnThumbnailClickListener.empty

  /**
   * Offers multiple ways to obtain [PostDetails] from the [post].
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
     * Converts the sample [Post] into [PostDetails] that have an exact amount of comments.
     *
     * @param commentCount Quantity of comments for the returned [PostDetails] to have.
     */
    fun withCommentCountOf(commentCount: Int): PostDetails {
      return withStats { copy(commentCount = commentCount) }
    }

    /** Converts the sample [Post] into favorited [PostDetails]. */
    fun favorited(): PostDetails {
      return withStats { copy(isFavorite = true) }
    }

    /** Converts the sample [Post] into unfavorited [PostDetails]. */
    fun unfavorited(): PostDetails {
      return withStats { copy(isFavorite = false) }
    }

    /** Converts the sample [Post] into reposted [PostDetails]. */
    fun reposted(): PostDetails {
      return withStats { copy(isReposted = true) }
    }

    /** Converts the [post] into unreposted [PostDetails]. */
    fun unreposted(): PostDetails {
      return withStats { copy(isReposted = false) }
    }

    /**
     * Converts the sample [Post] into a [Flow] of [PostDetails], to which emissions are performed
     * whenever one of its [Stat]s is updated.
     */
    fun asFlow(): Flow<PostDetails> {
      return post.toPostDetailsFlow(colors, onLinkClick, onThumbnailClickListener)
    }

    /**
     * Converts the sample [Post] into [PostDetails] to whose [StatsDetails] the [update] is
     * applied.
     *
     * @param update Updates the current [StatsDetails] of the [PostDetails] to which the sample
     *   [Post] has been converted.
     * @see PostDetails.stats
     */
    private fun withStats(update: StatsDetails.() -> StatsDetails): PostDetails {
      val details = post.toPostDetails(colors, onLinkClick, onThumbnailClickListener)
      val stats = details.stats.update()
      return details.copy(stats = stats)
    }
  }

  init {
    SampleInstance.Builder.create(
        authenticator,
        authenticationLock,
        feedProvider,
        profileProvider,
        profileSearcher,
        postProvider,
        imageLoaderProvider
      )
      .withDefaultProfiles()
      .withDefaultPosts()
  }

  /**
   * Obtains a [Detailing] through which [PostDetails] derived from the sample [Post] can be
   * obtained in various ways (i. e., as a single instance with a specific amount of comments or as
   * a [Flow]).
   *
   * @see Detailing.withCommentCountOf
   * @see Detailing.asFlow
   */
  fun detailed(): Detailing {
    return detailing
  }

  /**
   * Adds a comment to the sample [Post].
   *
   * @see uncomment
   */
  suspend fun comment() {
    post.comment.add(comment)
  }

  /**
   * Removes the sample comment added to the sample [Post].
   *
   * @see comment
   */
  suspend fun uncomment() {
    post.comment.remove(comment)
  }

  /**
   * Favorites the sample [Post].
   *
   * @see unfavorite
   */
  suspend fun favorite() {
    post.favorite.enable()
  }

  /**
   * Unfavorites the sample [Post].
   *
   * @see favorite
   */
  suspend fun unfavorite() {
    post.favorite.disable()
  }

  /**
   * Reposts the sample [Post].
   *
   * @see unrepost
   */
  suspend fun repost() {
    post.repost.enable()
  }

  /**
   * Unreposts the sample [Post].
   *
   * @see repost
   */
  suspend fun unrepost() {
    post.repost.disable()
  }
}

/**
 * Creates an environment for testing [Post]-to-[PostDetails] conversion.
 *
 * @param body Testing to be performed.
 * @see Post.id
 */
internal fun runPostDetailsConversionTest(body: suspend PostDetailsConversionScope.() -> Unit) {
  runTest { PostDetailsConversionScope(delegate = this).body() }
}
