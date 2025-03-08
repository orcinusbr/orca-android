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

package br.com.orcinus.orca.composite.timeline.test.post

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.performScrollToIndex
import br.com.orcinus.orca.composite.timeline.Timeline
import br.com.orcinus.orca.composite.timeline.post.PostPreview
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.GalleryPreview
import br.com.orcinus.orca.composite.timeline.post.figure.link.LinkCard
import br.com.orcinus.orca.composite.timeline.test.isTimeline
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.feed.SampleFeedProvider
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composer
import br.com.orcinus.orca.core.sample.instance.SampleInstance

/**
 * Scrolls to the first [PostPreview] containing a [GalleryPreview].
 *
 * @param feedProvider [SampleFeedProvider] that provides the feed in which the [Post] is expected
 *   to be.
 * @param onPost Action to be run on the underlying [Post] after scrolling to its index has been
 *   performed.
 * @throws AssertionError If the [SemanticsNode] isn't that of a [Timeline].
 * @throws NoSuchElementException If no matching [Post] is found.
 */
@Throws(AssertionError::class, NoSuchElementException::class)
fun SemanticsNodeInteraction.performScrollToPostPreviewWithGalleryPreview(
  feedProvider: SampleFeedProvider,
  onPost: (Post) -> Unit = {}
): SemanticsNodeInteraction {
  assert(isTimeline()) { "Can only scroll to the PostPreview with a GalleryPreview of a Timeline." }
  return performScrollToPostIndex(
    feedProvider,
    predicate = { _, foundPost -> foundPost.content.attachments.isNotEmpty() },
    onPost
  )
}

/**
 * Scrolls to the first [PostPreview] containing a [LinkCard].
 *
 * @param feedProvider [SampleFeedProvider] that provides the feed in which the [Post] is expected
 *   to be.
 * @throws AssertionError If the [SemanticsNode] isn't that of a [Timeline].
 * @throws NoSuchElementException If no matching [Post] is found.
 */
@Throws(AssertionError::class, NoSuchElementException::class)
fun SemanticsNodeInteraction.performScrollToPostPreviewWithLinkCard(
  feedProvider: SampleFeedProvider
) {
  assert(isTimeline()) { "Can only scroll to the PostPreview with a LinkCard of a Timeline." }
  performScrollToPostIndex(
    feedProvider,
    predicate = { _, foundPost -> foundPost.content.highlight != null }
  )
}

/**
 * Scrolls to the index of a [Post] from a [SampleInstance] that matches the [predicate].
 *
 * @param feedProvider [SampleFeedProvider] that provides the feed in which the [Post] is expected
 *   to be.
 * @param predicate Returns whether the index of the given [Post] should be the one to which
 *   scrolling will be performed.
 * @param onPost Action to be run on the [Post] that matched the [predicate] after scrolling to its
 *   index has been performed.
 * @throws NoSuchElementException If no matching [Post] is found.
 * @see SampleInstance
 */
@Throws(NoSuchElementException::class)
internal fun SemanticsNodeInteraction.performScrollToPostIndex(
  feedProvider: SampleFeedProvider,
  predicate: (inPagePosts: List<Post>, foundPost: Post) -> Boolean,
  onPost: (Post) -> Unit = {}
): SemanticsNodeInteraction {
  val (index, post) = findIndexedPost(feedProvider, predicate = predicate)
  return performScrollToIndex(index).also { onPost(post) }
}

/**
 * Finds a [Post] within a sample feed that matches the [predicate], returning it alongside its
 * index.
 *
 * @param feedProvider [SampleFeedProvider] that provides the feed in which the [Post] is expected
 *   to be.
 * @param page Feed page at which the [Post] will be looked for.
 * @param lastIndex Index of the last [Post] that's been provided while trying to find the matching
 *   one.
 * @param predicate Returns whether the given [Post] is the one to be provided.
 * @throws NoSuchElementException If no matching [Post] is found.
 * @see SampleFeedProvider
 * @see IndexedValue.index
 */
@Throws(NoSuchElementException::class)
private fun findIndexedPost(
  feedProvider: SampleFeedProvider,
  page: Int = 0,
  lastIndex: Int = 0,
  predicate: (inPagePosts: List<Post>, foundPost: Post) -> Boolean
): IndexedValue<Post> {
  return feedProvider
    .provideCurrent(page)
    .getValueOrThrow()
    .ifEmpty { throw NoSuchElementException("No post matching the given predicate was found.") }
    .let { inPagePosts ->
      inPagePosts
        .mapIndexed { index, post -> IndexedValue(lastIndex + index, post) }
        .find { (_, foundPost) -> predicate(inPagePosts, foundPost) }
    }
    ?: findIndexedPost(
      feedProvider,
      page.inc(),
      lastIndex = Composer.MAX_POST_COUNT_PER_PAGE * page.inc(),
      predicate
    )
}
