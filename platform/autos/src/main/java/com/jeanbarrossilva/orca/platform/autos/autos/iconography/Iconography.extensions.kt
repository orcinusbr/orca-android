package com.jeanbarrossilva.orca.platform.autos.autos.iconography

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.jeanbarrossilva.orca.autos.iconography.Iconography
import com.jeanbarrossilva.orca.platform.autos.autos.noLocalProvidedFor

/** [CompositionLocal] that provides an [Iconography]. */
internal val LocalIconography =
  compositionLocalOf<Iconography> { noLocalProvidedFor("LocalIconography") }
