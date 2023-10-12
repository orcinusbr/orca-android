package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.snack

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme

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
      @Composable get() = OrcaTheme.colors.surface.container
  }

  /**
   * Denotes that the [Snackbar] should be styled in a way that attempts to draw the attention of
   * the user to it, for notifying the occurrence of an error.
   */
  data class Error(override val message: String) : OrcaSnackbarVisuals() {
    override val actionLabel = "Retry"
    override val duration = SnackbarDuration.Indefinite

    override val containerColor
      @Composable get() = OrcaTheme.colors.error.container
  }
}
