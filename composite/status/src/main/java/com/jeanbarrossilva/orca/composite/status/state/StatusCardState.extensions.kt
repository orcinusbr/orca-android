/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.composite.status.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.jeanbarrossilva.orca.composite.status.Status
import kotlin.time.Duration
import kotlinx.coroutines.delay

/**
 * Remembers a [StatusCardState].
 *
 * @param targetStatus [Status] to which the current one will be changed when it loads.
 * @see StatusCardState.status
 * @see StatusCardState.loadStatus
 */
@Composable
fun rememberStatusCardState(
  targetStatus: Status = Status.Loading,
  delay: Duration = Duration.ZERO
): StatusCardState {
  /*
   * Regardless of the delay, given that the initial status of a state is the loading one, having it
   * also be the target status means that the actual status won't ever change at all when it is
   * requested to be "loaded" (that is, to be set as the specified target one).
   */
  val isEverLoading = remember(targetStatus) { targetStatus == Status.Loading }

  return remember(isEverLoading) {
      if (isEverLoading) {
        StatusCardState.EverLoading
      } else {
        StatusCardState(targetStatus)
      }
    }
    .also {
      LaunchedEffect(it, isEverLoading, delay) {
        if (!isEverLoading) {
          delay(delay)
          it.loadStatus()
        }
      }
    }
}
