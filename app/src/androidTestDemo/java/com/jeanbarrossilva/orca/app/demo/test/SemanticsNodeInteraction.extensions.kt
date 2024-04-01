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

package com.jeanbarrossilva.orca.app.demo.test

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.click
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import com.jeanbarrossilva.orca.composite.timeline.Timeline
import com.jeanbarrossilva.orca.composite.timeline.post.PostPreview
import com.jeanbarrossilva.orca.composite.timeline.post.figure.gallery.GalleryPreview
import com.jeanbarrossilva.orca.composite.timeline.stat.details.formatted
import com.jeanbarrossilva.orca.composite.timeline.test.isTimeline
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.SAMPLE_POSTS_PER_PAGE
import com.jeanbarrossilva.orca.feature.gallery.test.ui.page.isPage
import com.jeanbarrossilva.orca.feature.gallery.ui.Gallery
import com.jeanbarrossilva.orca.feature.gallery.ui.page.Index
import com.jeanbarrossilva.orca.feature.gallery.ui.page.Page
import com.jeanbarrossilva.orca.platform.core.sample
import com.jeanbarrossilva.orca.platform.testing.asString
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest

/**
 * Asserts that the content description of the [Page] to which the [SemanticsNode] refers is
 * cohesive to its position within the [Gallery].
 *
 * @param post [Post] from which the [Gallery] was composed.
 * @param entrypointIndex Index of the thumbnail that was clicked for navigation to the [Gallery] to
 *   be performed.
 * @throws AssertionError If the [SemanticsNode] isn't that of a [Page].
 */
internal fun SemanticsNodeInteraction.assertContentDescriptionIsCohesiveToPagePosition(
  post: Post,
  entrypointIndex: Int
): SemanticsNodeInteraction {
  assert(isPage()) {
    "Cannot assert the cohesiveness of the content description of a node that isn't that of a page."
  }
  val position = fetchSemanticsNode().config[SemanticsProperties.Index].inc()
  val isEntrypoint = entrypointIndex.inc() == position
  return assertContentDescriptionEquals(
    if (isEntrypoint) {
      com.jeanbarrossilva.orca.composite.timeline.R.string
        .composite_timeline_post_preview_gallery_thumbnail
        .asString(position.formatted, post.author.name)
    } else {
      com.jeanbarrossilva.orca.feature.gallery.R.string.feature_gallery_attachment.asString(
        position.formatted
      )
    }
  )
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
 * Scrolls to the first [PostPreview] containing a [GalleryPreview].
 *
 * @param onPost Action to be run on the underlying [Post] after scrolling to its index has been
 *   performed.
 * @throws AssertionError If the [SemanticsNode] isn't that of a [Timeline].
 */
internal fun SemanticsNodeInteraction.performScrollToPostPreviewWithGalleryPreview(
  onPost: (Post) -> Unit = {}
): SemanticsNodeInteraction {
  assert(isTimeline()) { "Can only scroll to the PostPreview with a GalleryPreview of a Timeline." }
  return performScrollToPostIndex({ it.content.attachments.isNotEmpty() }, onPost)
}

/** Performs a click on the portion located at [Offset.Zero] of this [SemanticsNode]. */
internal fun SemanticsNodeInteraction.performStartClick() {
  performTouchInput { click(Offset.Zero) }
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
