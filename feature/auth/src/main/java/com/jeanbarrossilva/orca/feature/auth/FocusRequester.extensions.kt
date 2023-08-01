package com.jeanbarrossilva.orca.feature.auth

import androidx.compose.ui.focus.FocusRequester
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

/** Requests focus, trying again if it fails. **/
internal suspend fun FocusRequester.tryToRequestFocus() {
    try {
        requestFocus()
    } catch (_: IllegalStateException) {
        delay(100.milliseconds)
        tryToRequestFocus()
    }
}
