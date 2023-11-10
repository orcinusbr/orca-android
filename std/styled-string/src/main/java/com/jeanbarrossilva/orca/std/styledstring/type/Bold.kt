package com.jeanbarrossilva.orca.std.styledstring.type

import com.jeanbarrossilva.orca.std.styledstring.Style

/** [Style] that applies a heavier weight to the target's font. */
data class Bold(override val indices: IntRange) : Style {
  override fun at(indices: IntRange): Bold {
    return copy(indices = indices)
  }
}
