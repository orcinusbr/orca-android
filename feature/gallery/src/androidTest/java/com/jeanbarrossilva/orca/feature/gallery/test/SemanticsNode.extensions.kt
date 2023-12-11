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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutInfo
import androidx.compose.ui.platform.ViewRootForTest
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers

/**
 * Whether this [SemanticsNode] is being displayed.
 *
 * **NOTE**: Most of the implementation is as [SemanticsNodeInteraction.isDisplayed]'s is, since no
 * version of it has been made publicly available specifically for [SemanticsNode]s as of version
 * 1.5.3 of Jetpack Compose. If it is officially provided in a future version, it is highly
 * recommended to migrate to it instead of relying on this one.
 */
internal val SemanticsNode.isDisplayed: Boolean
  get() {
    fun isNotPlaced(node: LayoutInfo): Boolean {
      return !node.isPlaced
    }

    fun SemanticsNode.clippedNodeBoundsInWindow(): Rect {
      val composeView = (root as ViewRootForTest).view
      val rootLocationInWindow =
        intArrayOf(0, 0).let { location ->
          composeView.getLocationInWindow(location)
          Offset(location[0].toFloat(), location[1].toFloat())
        }
      return boundsInRoot.translate(rootLocationInWindow)
    }

    fun SemanticsNode.isInScreenBounds(): Boolean {
      val composeView = (root as ViewRootForTest).view

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
     * Executes [selector] on every parent of this [LayoutInfo] and returns the closest [LayoutInfo]
     * to return `true` from [selector] or null if [selector] returns false for all ancestors.
     */
    fun LayoutInfo.findClosestParentNode(selector: (LayoutInfo) -> Boolean): LayoutInfo? {
      var currentParent = this.parentInfo
      while (currentParent != null) {
        if (selector(currentParent)) {
          return currentParent
        } else {
          currentParent = currentParent.parentInfo
        }
      }

      return null
    }

    val layoutInfo = layoutInfo
    if (isNotPlaced(layoutInfo) || layoutInfo.findClosestParentNode(::isNotPlaced) != null) {
      return false
    }

    (root as? ViewRootForTest)?.let { root ->
      if (!ViewMatchers.isDisplayed().matches(root.view)) {
        return false
      }
    }

    // check node doesn't clip unintentionally (e.g. row too small for content)
    val globalRect = boundsInWindow
    if (!isInScreenBounds()) {
      return false
    }

    return globalRect.width > 0f && globalRect.height > 0f
  }
