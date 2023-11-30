package com.jeanbarrossilva.orca.platform.ui.test.component.timeline.refresh

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

/** Asserts that the [SemanticsNode] isn't in a temporarily active state. */
fun SemanticsNodeInteraction.assertIsNotInProgress(): SemanticsNodeInteraction {
  return assert(isInProgress().not())
}
