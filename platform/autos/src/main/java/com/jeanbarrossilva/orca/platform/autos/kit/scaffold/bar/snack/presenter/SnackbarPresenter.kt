/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
