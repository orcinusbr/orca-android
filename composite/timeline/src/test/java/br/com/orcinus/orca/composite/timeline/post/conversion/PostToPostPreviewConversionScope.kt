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
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.feed.profile.post.createSample
import br.com.orcinus.orca.platform.core.image.sample
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
 */
internal class PostToPostPreviewConversionScope
@InternalPostToPostPreviewConversionApi
constructor(delegate: TestScope) : CoroutineScope by delegate {
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
    return Posts { add { Post.createSample(ComposableImageLoader.Provider.sample) } }.single()
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
    val conversionScope = PostToPostPreviewConversionScope(this)
    try {
      conversionScope.body()
    } finally {
      conversionScope.reset()
    }
  }
}
