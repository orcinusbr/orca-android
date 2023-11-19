package com.jeanbarrossilva.orca.platform.autos

import androidx.compose.runtime.CompositionLocal

/**
 * Throws an [IllegalStateException] stating that no [CompositionLocal] named [name] isn't present.
 *
 * @param name Name of the [CompositionLocal] that isn't present.
 */
internal fun noLocalProvidedFor(name: String): Nothing {
  error("No CompositionLocal for $name present.")
}
