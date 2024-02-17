/*
 * Copyright © 2023-2024 Orca
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

package com.jeanbarrossilva.orca.platform.autos.reactivity

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** Listens to changes on the availability of the utmost bottom portion of the displayed content. */
interface OnBottomAreaAvailabilityChangeListener {
  /** Provides the height of the UI component in the bottom area. */
  val height: Int

  /**
   * [StateFlow] to which the current offset in the Y-axis of the UI component in the bottom area is
   * emitted.
   */
  val yOffsetFlow: StateFlow<Float>

  /**
   * Callback run whenever the availability of the bottom area is changed.
   *
   * @param yOffset Amount of offset in the Y-axis to be applied to a UI component in the bottom
   *   area.
   */
  fun onBottomAreaAvailabilityChange(yOffset: Float)

  companion object {
    /** No-op [OnBottomAreaAvailabilityChangeListener]. */
    val empty =
      object : OnBottomAreaAvailabilityChangeListener {
        override val height = 0
        override val yOffsetFlow = MutableStateFlow(0f)

        override fun onBottomAreaAvailabilityChange(yOffset: Float) {}
      }
  }
}
