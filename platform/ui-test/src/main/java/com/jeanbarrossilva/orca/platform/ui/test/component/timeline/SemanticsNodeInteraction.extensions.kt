package com.jeanbarrossilva.orca.platform.ui.test.component.timeline

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.performScrollToNode
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline

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
