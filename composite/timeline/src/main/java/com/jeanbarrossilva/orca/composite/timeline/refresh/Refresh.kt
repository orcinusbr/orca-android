/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.composite.timeline.refresh

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.jeanbarrossilva.orca.composite.timeline.Timeline

/**
 * Swipe-to-refresh behavior configuration for a [Timeline].
 *
 * @param isInProgress Whether the [Timeline] is currently being refreshed.
 * @param indicatorOffset Amount of [Dp]s by which the refresh indicator should be offset in the
 *   y-axis.
 * @param listener [Listener] to be notified of refreshes.
 */
@Immutable
data class Refresh(val isInProgress: Boolean, val indicatorOffset: Dp, val listener: Listener) {
  /**
   * Listens to refreshes.
   *
   * @see onRefresh
   */
  fun interface Listener {
    /** Callback run whenever the [Timeline] is refreshed. */
    fun onRefresh()
  }

  init {
    require(indicatorOffset.isSpecified) {
      "Cannot offset the refresh indicator by an unspecified amount of DPs."
    }
  }

  companion object {
    /** Never-active, no-op [Refresh]. */
    internal val Disabled = Refresh(isInProgress = false, indicatorOffset = 0.dp) {}

    /**
     * [Refresh] that remains active indefinitely.
     *
     * @see isInProgress
     */
    val Indefinite = Refresh(isInProgress = true, indicatorOffset = 0.dp) {}
  }
}
