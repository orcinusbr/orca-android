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
