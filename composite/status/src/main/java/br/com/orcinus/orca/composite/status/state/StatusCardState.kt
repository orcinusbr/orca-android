/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.composite.status.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.orcinus.orca.composite.status.Status
import br.com.orcinus.orca.composite.status.StatusCard

/**
 * State by which the [Status] of a [StatusCard] is held and loaded.
 *
 * @param targetStatus [Status] to which the current one will be changed when it loads.
 * @see status
 * @see loadStatus
 */
class StatusCardState internal constructor(private val targetStatus: Status) {
  /**
   * Current [Status] of the [StatusCard]. It starts as loading by default and can be changed to the
   * target one through [loadStatus].
   *
   * @see Status.Loading
   * @see targetStatus
   */
  internal var status by mutableStateOf(Status.Loading)
    private set

  /**
   * Defines the current [Status] as the target one with which this [StatusCardState] has been
   * created if it's loading.
   *
   * @see status
   * @see targetStatus
   * @see Status.Loading
   */
  internal fun loadStatus() {
    if (status == Status.Loading) {
      status = targetStatus
    }
  }

  companion object {
    /**
     * An immutable [StatusCardState], with a never-changing loading [status].
     *
     * @see Status.Loading
     */
    @Stable internal val EverLoading = StatusCardState(targetStatus = Status.Loading)
  }
}
