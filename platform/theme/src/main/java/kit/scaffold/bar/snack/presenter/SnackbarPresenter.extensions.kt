package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.snack.presenter

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/** Remembers a [SnackbarPresenter]. **/
@Composable
fun rememberSnackbarPresenter(): SnackbarPresenter {
    val hostState = remember(::SnackbarHostState)
    return remember(hostState) { SnackbarPresenter(hostState) }
}
