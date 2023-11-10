package com.jeanbarrossilva.orca.std.styledstring.style.type

import com.jeanbarrossilva.orca.std.styledstring.style.Style
import java.net.URL

/** [Style] for referencing a username. */
data class Mention(override val indices: IntRange, override val url: URL) :
  Style.Constrained(), Link {
  override val regex = Regex("[a-zA-Z0-9._%+-]+")

  companion object
}
