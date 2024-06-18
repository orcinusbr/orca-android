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

package br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.theme.AutosTheme

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
