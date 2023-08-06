package com.jeanbarrossilva.orca.platform.ui.core

import androidx.compose.ui.focus.FocusRequester
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

/** Requests focus with an initial delay. **/
suspend fun FocusRequester.requestFocusWithDelay() {
    delay(256.milliseconds)
    requestFocus()
}
