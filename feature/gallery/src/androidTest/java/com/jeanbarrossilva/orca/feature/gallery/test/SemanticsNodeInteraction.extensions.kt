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
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performScrollToIndex
import com.jeanbarrossilva.orca.feature.gallery.Gallery

/**
 * Returns whether the [matcher] matches the [SemanticsNode] fetched from this
 * [SemanticsNodeInteraction].
 *
 * @param matcher [SemanticsMatcher] to match the [SemanticsNode] against.
 * @see SemanticsMatcher.matches
 * @see SemanticsNodeInteraction.fetchSemanticsNode
 */
internal operator fun SemanticsNodeInteraction.get(matcher: SemanticsMatcher): Boolean {
  val node = fetchSemanticsNode()
  return matcher.matches(node)
}

/**
 * Scrolls to each page of the [Gallery]'s [HorizontalPager] and runs the given [onSettling]
 * callback on them.
 *
 * @param onSettling Action to be run on the first page and the subsequent ones when they are
 *   scrolled to. Note that the [Int] it receives is the position of the page (which is index + 1)
 *   rather than its index; the position of the first one is 1, second's is 2, third's is 3, and so
 *   on.
 * @throws AssertionError If this [SemanticsNodeInteraction]'s [SemanticsNode] isn't that of a
 *   [Gallery]'s [HorizontalPager].
 */
context(ComposeTestRule)

@Throws(AssertionError::class)
internal fun SemanticsNodeInteraction.performScrollToEachPage(
  onSettling: SemanticsNodeInteraction.(position: Int) -> Unit
): SemanticsNodeInteraction {
  var index = 0
  while (true) {
    onPage().onSettling(index.inc())
    try {
      performScrollToPageAt(index)
      index++
    } catch (_: IllegalArgumentException) {
      break
    }
  }
  return this
}

/**
 * Scrolls to the page of the [Gallery]'s [HorizontalPager] at the given [index].
 *
 * @param index Index of the page to scroll to. Note that the page's position, which is [index] + 1,
 *   is what will be taken into account when performing the scroll.
 * @throws AssertionError If this [SemanticsNodeInteraction]'s [SemanticsNode] isn't that of a
 *   [Gallery]'s [HorizontalPager].
 */
context(ComposeTestRule)

@Throws(AssertionError::class)
internal fun SemanticsNodeInteraction.performScrollToPageAt(index: Int): SemanticsNodeInteraction {
  assert(isPager()) { "Can only scroll to a page of a Gallery's HorizontalPager." }
  val position = index.inc()
  return performScrollToIndex(position).also {
    @OptIn(ExperimentalTestApi::class) waitUntilExactlyOneExists(isPage())
  }
}
