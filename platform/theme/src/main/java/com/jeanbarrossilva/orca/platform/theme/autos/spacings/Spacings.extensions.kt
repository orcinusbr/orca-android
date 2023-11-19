package com.jeanbarrossilva.orca.platform.theme.autos.spacings

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.jeanbarrossilva.orca.autos.Spacings
import com.jeanbarrossilva.orca.platform.theme.autos.noLocalProvidedFor

/** [CompositionLocal] that provides [Spacings]. */
internal val LocalSpacings = compositionLocalOf<Spacings> { noLocalProvidedFor("LocalSpacings") }
