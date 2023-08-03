package com.jeanbarrossilva.orca.platform.ui.core

import androidx.compose.ui.focus.FocusRequester
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

/** Requests focus, trying again if it fails. **/
suspend fun FocusRequester.tryToRequestFocus() {
    try {
        requestFocus()
    } catch (_: IllegalStateException) {
        delay(100.milliseconds)
        tryToRequestFocus()
    }
}
