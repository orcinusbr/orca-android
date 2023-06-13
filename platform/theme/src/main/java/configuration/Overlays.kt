package com.jeanbarrossilva.mastodonte.platform.theme.configuration

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** [CompositionLocal] that provides [Overlays]. **/
internal val LocalOverlays = compositionLocalOf {
    Overlays.Unspecified
}

/**
 * Portions of the UI taken by an element that's hierarchically higher.
 *
 * @param fab [PaddingValues] consumed by the FAB.
 **/
data class Overlays internal constructor(val fab: PaddingValues) {
    companion object {
        /** [Overlays] with [Dp.Unspecified] values. **/
        internal val Unspecified = Overlays(fab = PaddingValues(Dp.Unspecified))

        /** [Overlays] that are provided by default. **/
        internal val Default = Overlays(fab = PaddingValues(bottom = 88.dp))
    }
}
