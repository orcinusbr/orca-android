/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.post.conversion

import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.post.figure.Figure
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.Disposition
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticationLock
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticator
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider
import br.com.orcinus.orca.core.sample.feed.SampleFeedProvider
import br.com.orcinus.orca.core.sample.feed.profile.SampleProfileProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.SamplePostProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.sample.feed.profile.search.SampleProfileSearcher
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import java.net.URI
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/**
 * [CoroutineScope] in which [Post]-to-[PostPreview] conversions can be tested.
 *
 * @param delegate [TestScope] for [CoroutineScope]-like behavior.
 * @property authenticator [SampleAuthenticator] that authenticates the [Actor].
 * @property authenticationLock [SampleAuthenticationLock] for requiring authentication.
 * @property postProvider [SamplePostProvider] in which the [post] is added and by which it is
 *   provided.
 */
internal class PostToPostPreviewConversionScope
@InternalPostToPostPreviewConversionApi
constructor(
  private val authenticator: SampleAuthenticator,
  private val authenticationLock: SampleAuthenticationLock,
  val postProvider: SamplePostProvider,
  delegate: TestScope
) : CoroutineScope by delegate {
  /**
   * [SampleProfileProvider] to create the [SampleInstance] from which the [Post] is created with.
   *
   * @see createPost
   */
  private val profileProvider = SampleProfileProvider()

  /**
   * [SampleProfileSearcher] to create the [SampleInstance] from which the [Post] is created with.
   *
   * @see createPost
   */
  private val profileSearcher = SampleProfileSearcher(profileProvider)

  /** [ImageLoader.Provider] that provides the [ImageLoader] by which images are to be loaded. */
  private val imageLoaderProvider = ComposableImageLoader.Provider.sample

  /**
   * [SampleFeedProvider] to create the [SampleInstance] from which the [Post] is created with.
   *
   * @see createPost
   */
  private val feedProvider =
    SampleFeedProvider(profileProvider, postProvider, SampleTermMuter(), imageLoaderProvider)

  /** [Post] to be converted. */
  var post = createPost()
    private set

  /** [Colors] with which the [post] is to be converted into a [PostPreview]. */
  val colors = Colors.LIGHT

  /**
   * Callback to be called for the resulting [PostPreview] into which the [post] is to be converted
   * when a link gets clicked.
   */
  val onLinkClick = { _: URI -> }

  /**
   * [Disposition.OnThumbnailClickListener] to be notified of clicks on the [PostPreview]'s
   * thumbnails.
   */
  val onThumbnailClickListener = Disposition.OnThumbnailClickListener.empty

  /** [Figure] of the [PostPreview] into which the [post] is to be converted. */
  val figure =
    Figure.of(post.id, post.author.name, post.content, onLinkClick, onThumbnailClickListener)

  /** Creates a sample [Post] on which conversion-testing can be performed. */
  @InternalPostToPostPreviewConversionApi
  fun createPost(): Post {
    return SampleInstance.Builder.create(
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
      .build()
      .postProvider
      .provideOneCurrent()
  }

  /** Undoes any changes made to the [post] and sets it back to its initial state. */
  @InternalPostToPostPreviewConversionApi
  fun reset() {
    post = createPost()
  }
}

/**
 * Runs a [Post]-to-[PostPreview] conversion test.
 *
 * @param body Lambda in which the testing is performed.
 */
@OptIn(ExperimentalContracts::class)
internal inline fun runPostToPostPreviewConversionTest(
  crossinline body: suspend PostToPostPreviewConversionScope.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  runTest {
    val authenticator = SampleAuthenticator()
    val actorProvider = SampleActorProvider()
    val authenticationLock = SampleAuthenticationLock(authenticator, actorProvider)
    val postProvider = SamplePostProvider(authenticationLock)
    val conversionScope =
      PostToPostPreviewConversionScope(authenticator, authenticationLock, postProvider, this)
    try {
      conversionScope.body()
    } finally {
      conversionScope.reset()
    }
  }
}
