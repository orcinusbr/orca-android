package com.jeanbarrossilva.orca.platform.autos.kit.input.option.list.test

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher

/**
 * [SemanticsMatcher] that indicates whether the [Shape] by which the [SemanticsNode] has been
 * clipped is the given one.
 *
 * @param shape [Shape] to assert that it's the one by which the [SemanticsNode] is shaped.
 */
internal fun isShapedBy(shape: Shape): SemanticsMatcher {
  return SemanticsMatcher("Shape = '$shape'") { node ->
    shape ==
      node.layoutInfo
        .getModifierInfo()
        .map(ModifierInfo::modifier)
        .filterIsInstance<ModifierNodeElement<*>>()
        .flatMap(ModifierNodeElement<*>::inspectableElements)
        .find { element -> element.name == "shape" }
        ?.value
  }
}
