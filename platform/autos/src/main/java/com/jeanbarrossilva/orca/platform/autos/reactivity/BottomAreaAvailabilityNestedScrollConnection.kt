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

package com.jeanbarrossilva.orca.platform.autos.reactivity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

/**
 * [NestedScrollConnection] that notifies the [listener] of scroll changes.
 *
 * @param listener [OnBottomAreaAvailabilityChangeListener] to be notified.
 */
class BottomAreaAvailabilityNestedScrollConnection
internal constructor(private val listener: OnBottomAreaAvailabilityChangeListener) :
  NestedScrollConnection {
  override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
    val currentOffsetY = listener.yOffsetFlow.value
    val heightAsFloat = listener.height.toFloat()
    val changedOffsetY = (currentOffsetY - available.y).coerceIn(0f, heightAsFloat)
    listener.onBottomAreaAvailabilityChange(changedOffsetY)
    return Offset.Zero
  }

  companion object {
    /**
     * [BottomAreaAvailabilityNestedScrollConnection] with an empty
     * [OnBottomAreaAvailabilityChangeListener].
     *
     * @see OnBottomAreaAvailabilityChangeListener.empty
     */
    val empty =
      BottomAreaAvailabilityNestedScrollConnection(OnBottomAreaAvailabilityChangeListener.empty)
  }
}
