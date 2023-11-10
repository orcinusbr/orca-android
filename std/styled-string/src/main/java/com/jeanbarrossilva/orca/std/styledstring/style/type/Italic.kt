package com.jeanbarrossilva.orca.std.styledstring.style.type

import com.jeanbarrossilva.orca.std.styledstring.style.Style

/** [Style] that slightly slants the target [String] to the right. */
data class Italic(override val indices: IntRange) : Style {
  override fun at(indices: IntRange): Style {
    return copy(indices = indices)
  }
}
