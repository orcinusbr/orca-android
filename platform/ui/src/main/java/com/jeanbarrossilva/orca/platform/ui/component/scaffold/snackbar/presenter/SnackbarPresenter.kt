package com.jeanbarrossilva.orca.platform.ui.component.scaffold.snackbar.presenter

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.snackbar.OrcaSnackbarVisuals

/** Presents various types of [Snackbar]s. **/
class SnackbarPresenter internal constructor(internal val hostState: SnackbarHostState) {
    /**
     * Shows a [Snackbar] with the given [message], providing the user information about an error that
     * has occurred and drawing their attention to it.
     *
     * @param message Text to be displayed in the [Snackbar].
     **/
    suspend fun presentInfo(message: String) {
        val visuals = OrcaSnackbarVisuals.Info(message)
        hostState.showSnackbar(visuals)
    }

    /**
     * Shows a [Snackbar] with the given [message], providing the user additional information.
     *
     * @param message Text to be displayed in the [Snackbar].
     **/
    suspend fun presentError(message: String) {
        val visuals = OrcaSnackbarVisuals.Error(message)
        hostState.showSnackbar(visuals)
    }
}
