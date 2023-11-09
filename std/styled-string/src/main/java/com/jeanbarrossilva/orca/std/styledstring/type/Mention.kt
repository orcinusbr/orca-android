package com.jeanbarrossilva.orca.std.styledstring.type

import com.jeanbarrossilva.orca.std.styledstring.Style
import java.net.URL

/** [Style] for referencing a username. */
data class Mention(override val indices: IntRange, override val url: URL) : Link() {
  /** [Style.Delimiter] for [Mention]s. */
  sealed class Delimiter : Style.Delimiter() {
    /**
     * [Delimiter] that's the [parent] of all [Variant]s and considers [Mention]s parts of a
     * [String] starting with a [Mention.SYMBOL] and determines its end by the absence of
     * alphanumeric or some other special characters.
     */
    internal object Default : Delimiter() {
      override val parent = null

      override fun getRegex(): Regex {
        return Regex(SYMBOL + "$targetRegex")
      }

      override fun onGetTarget(match: String): String {
        return match.removePrefix("$SYMBOL")
      }

      override fun onTarget(target: String): String {
        return SYMBOL + target
      }
    }

    /** [Delimiter] that's a child of [Default]. */
    abstract class Variant : Delimiter() {
      final override val parent = Default
    }
  }

  companion object {
    /** [Char] that indicates the start of a [Mention] by default. */
    internal const val SYMBOL = '@'

    /** [Regex] that matches a [Mention]'s target. */
    val targetRegex = Regex("[a-zA-Z0-9._%+-]+")
  }
}
