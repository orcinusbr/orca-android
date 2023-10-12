package com.jeanbarrossilva.orca.platform.theme.configuration.iconography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.jeanbarrossilva.orca.platform.theme.extensions.Empty

/**
 * Houses different versions of the same icon.
 *
 * @param outlined [ImageVector] that is outlined.
 * @param filled [ImageVector] that is filled.
 */
@Immutable
data class Icon internal constructor(val outlined: ImageVector, val filled: ImageVector) {
  companion object {
    /** [Icon] with [ImageVector.Companion.Empty] values. */
    val Empty = Icon(outlined = ImageVector.Empty, filled = ImageVector.Empty)
  }
}
