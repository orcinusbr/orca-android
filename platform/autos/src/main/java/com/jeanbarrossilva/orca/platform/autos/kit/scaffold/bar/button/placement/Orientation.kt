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

import androidx.compose.material3.Button
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.IntOffset
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.ButtonBar

/** Indicates how [Button]s in a [ButtonBar] are placed in relation to each other. */
internal enum class Orientation {
  /** [Button]s are placed below each other. */
  VERTICAL {
    override fun getOffset(placement: Placement): IntOffset {
      return IntOffset(x = 0, y = placement.axisOffset)
    }

    override fun getDimension(placeable: Placeable): Int {
      return placeable.height
    }
  };

  /**
   * Gets the [IntOffset] by which the [placement] will be shifted in the axis that's appropriate
   * for this specific [Orientation].
   */
  abstract fun getOffset(placement: Placement): IntOffset

  /**
   * Gets the dimension (width or height) to be considered when placing the [placeable].
   *
   * @param placeable [Placeable] whose appropriate dimension will be obtained.
   */
  abstract fun getDimension(placeable: Placeable): Int
}
