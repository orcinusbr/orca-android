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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.snack.presenter

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.snack.OrcaSnackbarVisuals

/** Presents various types of [Snackbar]s. */
class SnackbarPresenter internal constructor(internal val hostState: SnackbarHostState) {
  /**
   * Listens to a request to perform a failed operation again.
   *
   * @see onRetry
   */
  fun interface OnRetryListener {
    /** Callback run if a failed operation is requested to be performed again. */
    suspend fun onRetry()
  }

  /**
   * Shows a [Snackbar] with the given [message], providing the user information about an error that
   * has occurred and drawing their attention to it.
   *
   * @param message Text to be displayed in the [Snackbar].
   */
  suspend fun presentInfo(message: String) {
    val visuals = OrcaSnackbarVisuals.Info(message)
    hostState.showSnackbar(visuals)
  }

  /**
   * Shows a [Snackbar] with the given [message], providing the user additional information.
   *
   * @param message Text to be displayed in the [Snackbar].
   * @param onRetryListener [OnRetryListener] to be notified when a failed operation is requested to
   *   be performed again.
   */
  suspend fun presentError(message: String, onRetryListener: OnRetryListener) {
    val visuals = OrcaSnackbarVisuals.Error(message)
    val result = hostState.showSnackbar(visuals)
    if (result == SnackbarResult.ActionPerformed) {
      onRetryListener.onRetry()
    }
  }
}
