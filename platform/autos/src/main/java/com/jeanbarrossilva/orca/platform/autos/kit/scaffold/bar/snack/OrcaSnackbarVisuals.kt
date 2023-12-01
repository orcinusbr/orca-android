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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.snack

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/**
 * [SnackbarVisuals] encompassing multiple scenarios in which the [Snackbar] will be presented
 * uniquely, such as when it merely displays useful information to the user or when it warns them
 * about an error that has occurred.
 *
 * @see Info
 * @see Error
 */
internal sealed class OrcaSnackbarVisuals : SnackbarVisuals {
  override val withDismissAction = false

  /** [Color] of the [Snackbar] container. */
  @get:Composable abstract val containerColor: Color

  /** [Color] of the [Snackbar] content. */
  val contentColor
    @Composable get() = contentColorFor(containerColor)

  /** Denotes that the [Snackbar] should be styled in a non-intrusive manner. */
  data class Info(override val message: String) : OrcaSnackbarVisuals() {
    override val actionLabel = null
    override val duration = SnackbarDuration.Short

    override val containerColor
      @Composable get() = AutosTheme.colors.surface.container.asColor
  }

  /**
   * Denotes that the [Snackbar] should be styled in a way that attempts to draw the attention of
   * the user to it, for notifying the occurrence of an error.
   */
  data class Error(override val message: String) : OrcaSnackbarVisuals() {
    override val actionLabel = "Retry"
    override val duration = SnackbarDuration.Indefinite

    override val containerColor
      @Composable get() = AutosTheme.colors.error.container.asColor
  }
}
