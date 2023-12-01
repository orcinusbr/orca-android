/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
