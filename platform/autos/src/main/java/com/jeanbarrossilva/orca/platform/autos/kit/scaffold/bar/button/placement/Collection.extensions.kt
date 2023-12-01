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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.placement

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints

/** Height of all [Placement]s combined. */
internal val Collection<Placement>.height
  get() = sumOf { placement -> placement.placeable.height }

/**
 * Maps each [Measurable] to a [Placement].
 *
 * @param constraints Measuring that will limit the [Measurable]s' measured size.
 * @param orientation [Orientation] in which the resulting [Placement]s will be placed.
 * @param spacing Size of the space between each of the [Placement]s in pixels.
 */
internal fun Collection<Measurable>.mapToPlacement(
  constraints: Constraints,
  orientation: Orientation,
  spacing: Int
): List<Placement> {
  var offset = 0
  return map { measurable ->
    measurable.toPlacement(constraints, offset).also { placement ->
      offset += orientation.getDimension(placement.placeable) + spacing
    }
  }
}
