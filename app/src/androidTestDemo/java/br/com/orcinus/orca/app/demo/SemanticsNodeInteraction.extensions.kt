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

package br.com.orcinus.orca.app.demo

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionEquals
import br.com.orcinus.orca.composite.timeline.stat.details.formatted
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.feature.gallery.test.ui.page.isPage
import br.com.orcinus.orca.feature.gallery.ui.Gallery
import br.com.orcinus.orca.feature.gallery.ui.page.Index
import br.com.orcinus.orca.feature.gallery.ui.page.Page
import br.com.orcinus.orca.platform.testing.asString

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
      br.com.orcinus.orca.composite.timeline.R.string
        .composite_timeline_post_preview_gallery_thumbnail
        .asString(position.formatted, post.author.name)
    } else {
      br.com.orcinus.orca.feature.gallery.R.string.feature_gallery_attachment.asString(
        position.formatted
      )
    }
  )
}
