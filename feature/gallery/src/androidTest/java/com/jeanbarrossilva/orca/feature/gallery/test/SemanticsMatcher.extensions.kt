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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutInfo
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers
import com.jeanbarrossilva.orca.feature.gallery.GALLERY_PAGER_TAG
import com.jeanbarrossilva.orca.feature.gallery.Gallery

/**
 * [SemanticsMatcher] that matches a displayed [SemanticsNode].
 *
 * **NOTE**: Most of the implementation is as [SemanticsNodeInteraction.isDisplayed]'s is, since no
 * version of it has been made publicly available specifically for strictly-assertion-driven
 * scenarios (such an `isDisplayed()` [SemanticsMatcher] like this one) as of version 1.5.3 of
 * Jetpack Compose. If it is to be officially provided in an adopted future version, it is highly
 * recommended to migrate to it instead of relying on this one.
 */
internal fun isDisplayed(): SemanticsMatcher {
  return SemanticsMatcher("is displayed") { node ->
    fun isNotPlaced(layoutInfo: LayoutInfo): Boolean {
      return !layoutInfo.isPlaced
    }

    fun clippedNodeBoundsInWindow(): Rect {
      val composeView = (node.root as ViewRootForTest).view
      val rootLocationInWindow =
        intArrayOf(0, 0).let { location ->
          composeView.getLocationInWindow(location)
          Offset(location[0].toFloat(), location[1].toFloat())
        }
      return node.boundsInRoot.translate(rootLocationInWindow)
    }

    fun isNodeInScreenBounds(): Boolean {
      val composeView = (node.root as ViewRootForTest).view

      // Window relative bounds of our node
      val nodeBoundsInWindow = clippedNodeBoundsInWindow()
      if (nodeBoundsInWindow.width == 0f || nodeBoundsInWindow.height == 0f) {
        return false
      }

      // Window relative bounds of our compose root view that are visible on the screen
      val globalRootRect = android.graphics.Rect()
      if (!composeView.getGlobalVisibleRect(globalRootRect)) {
        return false
      }
      return !nodeBoundsInWindow
        .intersect(
          Rect(
            globalRootRect.left.toFloat(),
            globalRootRect.top.toFloat(),
            globalRootRect.right.toFloat(),
            globalRootRect.bottom.toFloat()
          )
        )
        .isEmpty
    }

    /**
     * Returns the closest [LayoutInfo] that hasn't been placed or `null` if all of the [node]'s
     * parents have been placed.
     */
    fun findClosestUnplacedParentNode(): LayoutInfo? {
      var currentParent = node.layoutInfo.parentInfo
      while (currentParent != null) {
        if (isNotPlaced(currentParent)) {
          return currentParent
        } else {
          currentParent = currentParent.parentInfo
        }
      }

      return null
    }

    if (isNotPlaced(node.layoutInfo) || findClosestUnplacedParentNode() != null) {
      return@SemanticsMatcher false
    }
    (node.root as? ViewRootForTest)?.let { root ->
      if (!ViewMatchers.isDisplayed().matches(root.view)) {
        return@SemanticsMatcher false
      }
    }

    // check node doesn't clip unintentionally (e.g. row too small for content)
    val globalRect = node.boundsInWindow
    if (!isNodeInScreenBounds()) {
      return@SemanticsMatcher false
    }
    globalRect.width > 0f && globalRect.height > 0f
  }
}

/** [SemanticsMatcher] that matches a [Gallery]'s [HorizontalPager]. */
internal fun isPager(): SemanticsMatcher {
  return hasTestTag(GALLERY_PAGER_TAG)
}
