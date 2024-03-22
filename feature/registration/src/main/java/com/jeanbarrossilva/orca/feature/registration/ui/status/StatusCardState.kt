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

package com.jeanbarrossilva.orca.feature.registration.ui.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.time.Duration
import kotlinx.coroutines.delay

internal class StatusCardState(initialStatus: Status) {
  var status by mutableStateOf(initialStatus)
}

@Composable
internal fun rememberStatusCardState(delay: Duration, targetStatus: Status): StatusCardState {
  val state = rememberStatusCardState()

  LaunchedEffect(Unit) {
    delay(delay)
    state.status = targetStatus
  }

  return state
}

@Composable
internal fun rememberStatusCardState(initialStatus: Status = Status.Loading): StatusCardState {
  return remember { StatusCardState(initialStatus) }
}
