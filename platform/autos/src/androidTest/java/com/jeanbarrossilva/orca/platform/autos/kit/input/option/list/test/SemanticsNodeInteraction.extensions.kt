package com.jeanbarrossilva.orca.platform.autos.kit.input.option.list.test

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

/**
 * Asserts that the [SemanticsNode] has been clipped by the given [shape].
 *
 * @param shape [Shape] to assert that it's the one by which the [SemanticsNode] is shaped.
 */
internal fun SemanticsNodeInteraction.assertIsShapedBy(shape: Shape): SemanticsNodeInteraction {
  return assert(isShapedBy(shape))
}
