/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.feature.gallery.test.ui

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performScrollToIndex
import com.jeanbarrossilva.orca.feature.gallery.test.ui.page.isPage
import com.jeanbarrossilva.orca.feature.gallery.test.ui.page.onPage
import com.jeanbarrossilva.orca.feature.gallery.ui.Gallery
import com.jeanbarrossilva.orca.feature.gallery.ui.page.Index

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
fun SemanticsNodeInteraction.performScrollToEachPage(
  onSettling: SemanticsNodeInteraction.(position: Int) -> Unit = {}
): SemanticsNodeInteraction {
  var page = onPage()
  val getPageIndex = { page.fetchSemanticsNode().config[SemanticsProperties.Index] }
  var index = getPageIndex()
  while (true) {
    page.onSettling(index.inc())
    try {
      performScrollToPageAfter(index)
      page = onPage()
      index = getPageIndex()
    } catch (_: IndexOutOfBoundsException) {
      break
    }
  }
  return this
}

/**
 * Scrolls to the page of the [Gallery]'s [HorizontalPager] at the position of the given [index].
 *
 * @param index Index of the page to scroll to. Note that the page's position, which is [index] + 1,
 *   is what will be taken into account when performing the scroll.
 * @throws AssertionError If this [SemanticsNodeInteraction]'s [SemanticsNode] isn't that of a
 *   [Gallery]'s [HorizontalPager].
 * @throws IndexOutOfBoundsException If there is no page at the position of the [index].
 */
context(ComposeTestRule)

@Throws(AssertionError::class)
internal fun SemanticsNodeInteraction.performScrollToPageAfter(
  index: Int
): SemanticsNodeInteraction {
  assert(isPager()) { "Can only scroll to a page of a Gallery's HorizontalPager." }
  val position = index.inc()
  return try {
    performScrollToIndex(position).also {
      @OptIn(ExperimentalTestApi::class) waitUntilExactlyOneExists(isPage())
    }
  } catch (e: IllegalArgumentException) {
    throw IndexOutOfBoundsException("Index out of range: $index.")
  }
}
