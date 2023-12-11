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

package com.jeanbarrossilva.orca.feature.gallery.test

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onParent
import androidx.compose.ui.unit.Density
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.feature.gallery.GALLERY_PAGER_TAG
import com.jeanbarrossilva.orca.feature.gallery.Gallery

/**
 * [SemanticsMatcher] that matches a displayed [SemanticsNode].
 *
 * @see SemanticsNode.isDisplayed
 */
internal fun isDisplayed(): SemanticsMatcher {
  return SemanticsMatcher("is displayed", SemanticsNode::isDisplayed)
}

/** [SemanticsMatcher] that matches a [Gallery]'s [HorizontalPager]. */
internal fun isPager(): SemanticsMatcher {
  return hasTestTag(GALLERY_PAGER_TAG)
}

/** [SemanticsMatcher] that matches a zoomed in [SemanticsNode]. */
context(SemanticsNodeInteraction)

internal fun isZoomedIn(): SemanticsMatcher {
  return SemanticsMatcher("is zoomed in") {
    val context = InstrumentationRegistry.getInstrumentation().context
    val density = Density(context)
    val bounds = with(density) { getUnclippedBoundsInRoot().toRect() }
    val parentBounds = with(density) { onParent().getUnclippedBoundsInRoot().toRect() }
    bounds.top < parentBounds.top && bounds.bottom < parentBounds.bottom
  }
}
