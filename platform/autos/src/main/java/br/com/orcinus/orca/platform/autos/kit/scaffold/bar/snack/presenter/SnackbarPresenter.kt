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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.OrcaSnackbarVisuals

/** Presents various types of [Snackbar]s. */
class SnackbarPresenter internal constructor(internal val hostState: SnackbarHostState) {
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
   */
  suspend fun presentError(message: String) {
    val visuals = OrcaSnackbarVisuals.Error(message)
    hostState.showSnackbar(visuals)
  }
}
