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

package com.jeanbarrossilva.orca.app.demo.test

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.click
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.gallery.GalleryPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.isTimeline
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

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
  lateinit var indexedPost: IndexedValue<Post>
  runTest {
    indexedPost =
      Instance.sample.feedProvider
        .provide(Actor.Authenticated.sample.id, page = 0)
        .first()
        .withIndex()
        .first { predicate(it.value) }
  }
  return performScrollToIndex(indexedPost.index).also { onPost(indexedPost.value) }
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
  return performScrollToPostIndex({ it.content.highlight != null }, onPost)
}

/** Performs a click on the portion located at [Offset.Zero] of this [SemanticsNode]. */
internal fun SemanticsNodeInteraction.performStartClick() {
  performTouchInput { click(Offset.Zero) }
}
