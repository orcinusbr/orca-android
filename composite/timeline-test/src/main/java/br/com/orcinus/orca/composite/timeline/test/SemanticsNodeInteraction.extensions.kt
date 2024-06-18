/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.composite.timeline.test

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.performScrollToNode
import br.com.orcinus.orca.composite.timeline.Timeline

/**
 * Scrolls to the bottom of the [Timeline] to which the [SemanticsNode] refers to.
 *
 * @throws AssertionError If the [SemanticsNode] isn't that of a [Timeline].
 */
@Throws(AssertionError::class)
fun SemanticsNodeInteraction.performScrollToBottom(): SemanticsNodeInteraction {
  assert(isTimeline()) { "Can only scroll to the bottom of a Timeline." }
  return performScrollToNode(isRenderEffect())
}
