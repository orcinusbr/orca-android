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
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.feed.profile.SAMPLE_POSTS_PER_PAGE
import br.com.orcinus.orca.platform.core.sample
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest

/**
 * Scrolls to the first [PostPreview] containing a [GalleryPreview].
 *
 * @param onPost Action to be run on the underlying [Post] after scrolling to its index has been
 *   performed.
 * @throws AssertionError If the [SemanticsNode] isn't that of a [Timeline].
 */
@Throws(AssertionError::class)
fun SemanticsNodeInteraction.performScrollToPostPreviewWithGalleryPreview(
  onPost: (Post) -> Unit = {}
): SemanticsNodeInteraction {
  assert(isTimeline()) { "Can only scroll to the PostPreview with a GalleryPreview of a Timeline." }
  return performScrollToPostIndex({ it.content.attachments.isNotEmpty() }, onPost)
}

/**
 * Scrolls to the first [PostPreview] containing a [LinkCard].
 *
 * @throws AssertionError If the [SemanticsNode] isn't that of a [Timeline].
 */
@Throws(AssertionError::class)
fun SemanticsNodeInteraction.performScrollToPostPreviewWithLinkCard() {
  assert(isTimeline()) { "Can only scroll to the PostPreview with a LinkCard of a Timeline." }
  performScrollToPostIndex({ it.content.highlight != null })
}

/**
 * Scrolls to the index of a [Post] from the sample [Instance] that matches the [predicate].
 *
 * @param predicate Returns whether the index of the given [Post] should be the one to which
 *   scrolling will be performed.
 * @param onPost Action to be run on the [Post] that matched the [predicate] after scrolling to its
 *   index has been performed.
 * @see Instance.Companion.sample
 */
internal fun SemanticsNodeInteraction.performScrollToPostIndex(
  predicate: (Post) -> Boolean,
  onPost: (Post) -> Unit = {}
): SemanticsNodeInteraction {
  var indexedPost: IndexedValue<Post>? = null
  runTest { indexedPost = findIndexedPost(predicate = predicate) }
  return indexedPost?.let { (index, post) -> performScrollToIndex(index).also { onPost(post) } }
    ?: this
}

/**
 * Finds a [Post] within the sample feed that matches the [predicate], returning it alongside its
 * index.
 *
 * @param page Feed page at which the [Post] will be looked for.
 * @param lastIndex Index of the last [Post] that's been provided while trying to find the matching
 *   one.
 * @param predicate Returns whether the given [Post] is the one to be provided.
 * @see Instance.Companion.sample
 * @see Instance.feedProvider
 * @see IndexedValue.index
 */
private suspend fun findIndexedPost(
  page: Int = 0,
  lastIndex: Int = 0,
  predicate: (Post) -> Boolean
): IndexedValue<Post> {
  return Instance.sample.feedProvider
    .provide(Actor.Authenticated.sample.id, page)
    .firstOrNull()
    ?.mapIndexed { index, post -> IndexedValue(lastIndex + index, post) }
    ?.find { (_, post) -> predicate(post) }
    ?: findIndexedPost(page.inc(), lastIndex = SAMPLE_POSTS_PER_PAGE * page.inc(), predicate)
}
