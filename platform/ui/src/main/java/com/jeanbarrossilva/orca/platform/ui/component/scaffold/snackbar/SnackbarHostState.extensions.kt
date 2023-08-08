package com.jeanbarrossilva.orca.platform.ui.component.scaffold.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState

/**
 * Shows a [Snackbar] with the given [message], providing the user information about an error that
 * has occurred and drawing their attention to it.
 *
 * @param message Text to be displayed in the [Snackbar].
 **/
suspend fun SnackbarHostState.showErrorSnackbar(message: String) {
    val visuals = OrcaSnackbarVisuals.Error(message)
    showSnackbar(visuals)
}

/**
 * Shows a [Snackbar] with the given [message], providing the user additional information.
 *
 * @param message Text to be displayed in the [Snackbar].
 **/
suspend fun SnackbarHostState.showInfoSnackbar(message: String) {
    val visuals = OrcaSnackbarVisuals.Info(message)
    showSnackbar(visuals)
}
