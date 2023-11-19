package com.jeanbarrossilva.orca.platform.theme.autos.overlays

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.jeanbarrossilva.orca.autos.overlays.Overlays
import com.jeanbarrossilva.orca.platform.theme.autos.noLocalProvidedFor

/** [CompositionLocal] that provides [Overlays]. */
internal val LocalOverlays = compositionLocalOf<Overlays> { noLocalProvidedFor("LocalOverlays") }
