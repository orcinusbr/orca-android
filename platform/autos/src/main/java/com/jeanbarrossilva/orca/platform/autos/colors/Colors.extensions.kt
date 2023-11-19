package com.jeanbarrossilva.orca.platform.autos.colors

import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.jeanbarrossilva.orca.autos.colors.Colors
import com.jeanbarrossilva.orca.platform.autos.noLocalProvidedFor

/** [CompositionLocal] that provides [Colors]. */
internal val LocalColors = compositionLocalOf<Colors> { noLocalProvidedFor("LocalColors") }
