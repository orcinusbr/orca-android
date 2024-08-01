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

package br.com.orcinus.orca.platform.autos.overlays.refresh

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember

/**
 * Swipe-to-refresh behavior configuration for a [Refreshable].
 *
 * @param isInProgress Whether the [Refreshable] is currently being refreshed.
 * @param listener [Listener] to be notified of refreshes.
 */
@Immutable
data class Refresh(val isInProgress: Boolean, val listener: Listener) {
  /**
   * Listens to refreshes.
   *
   * @see onRefresh
   */
  fun interface Listener {
    /** Callback run whenever the [Refreshable] is refreshed. */
    fun onRefresh()

    companion object {
      /** A no-op [Listener]. */
      val Empty = Listener {}
    }
  }

  companion object {
    /** Never-active, no-op [Refresh]. */
    val Disabled = Refresh(isInProgress = false, Listener.Empty)

    /**
     * [Refresh] that remains active indefinitely.
     *
     * @see isInProgress
     */
    val Indefinite = Refresh(isInProgress = true, Listener.Empty)

    /**
     * Creates a [Refresh] that can be active but doesn't report its actual progress.
     *
     * @param listener [Listener] to be notified of refreshes.
     */
    @Composable
    fun immediate(listener: Listener): Refresh {
      return remember { Refresh(isInProgress = false) { listener.onRefresh() } }
    }
  }
}
