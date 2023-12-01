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

import androidx.compose.ui.layout.Placeable

/**
 * Places the given [placements].
 *
 * @param placements [Placement]s to be placed.
 * @param orientation [Orientation] in which the [placements] will be placed.
 */
internal fun Placeable.PlacementScope.place(placements: List<Placement>, orientation: Orientation) {
  placements.forEach { place(it, orientation) }
}

/**
 * Places the given [placement].
 *
 * @param placement [Placement] to be placed.
 * @param orientation [Orientation] in which the [placement] will be placed.
 */
internal fun Placeable.PlacementScope.place(placement: Placement, orientation: Orientation) {
  val offset = orientation.getOffset(placement)
  placement.placeable.place(offset)
}
