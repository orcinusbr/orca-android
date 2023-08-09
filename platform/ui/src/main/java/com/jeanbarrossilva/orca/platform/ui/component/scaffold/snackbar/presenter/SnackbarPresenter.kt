package com.jeanbarrossilva.orca.platform.ui.component.scaffold.snackbar.presenter

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.snackbar.OrcaSnackbarVisuals

/** Presents various types of [Snackbar]s. **/
class SnackbarPresenter internal constructor(internal val hostState: SnackbarHostState) {
    /**
     * Listens to a request to perform a failed operation again.
     *
     * @see onRetry
     **/
    fun interface OnRetryListener {
        /** Callback run if a failed operation is requested to be performed again. **/
        suspend fun onRetry()
    }

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
     * @param onRetryListener [OnRetryListener] to be notified when a failed operation is requested
     * to be performed again.
     **/
    suspend fun presentError(message: String, onRetryListener: OnRetryListener) {
        val visuals = OrcaSnackbarVisuals.Error(message)
        val result = hostState.showSnackbar(visuals)
        if (result == SnackbarResult.ActionPerformed) {
            onRetryListener.onRetry()
        }
    }
}
