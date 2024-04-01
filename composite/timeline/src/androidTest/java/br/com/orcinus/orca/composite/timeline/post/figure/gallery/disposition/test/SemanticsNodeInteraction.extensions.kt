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

package br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.test

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition.AspectRatio
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.thumbnail.Thumbnail
import br.com.orcinus.orca.composite.timeline.test.post.figure.gallery.thumbnail.isThumbnail

/**
 * Asserts that the [Thumbnail]'s aspect ratio is equal to the given one.
 *
 * @param aspectRatio Aspect ratio that the [Thumbnail] is expected to have.
 */
internal fun SemanticsNodeInteraction.assertAspectRatioEquals(
  aspectRatio: Float
): SemanticsNodeInteraction {
  assert(isThumbnail()) { "Can only assert the aspect ratio of a Thumbnail." }
  val matcher =
    SemanticsMatcher("AspectRatio = $aspectRatio") {
      it.config[SemanticsProperties.AspectRatio] == aspectRatio
    }
  return assert(matcher)
}
