package com.jeanbarrossilva.orca.std.styledstring.style.type

import com.jeanbarrossilva.orca.std.styledstring.style.Style
import java.net.URL
import java.util.Objects

/**
 * [Style] that attaches a [URL] to a text.
 *
 * @see url
 */
interface Link : Style {
  /** [URL] to which this [Link] links. */
  val url: URL

  override fun at(indices: IntRange): Style {
    return to(url, indices)
  }

  companion object {
    /** [Regex] that matches a [URL]. */
    internal val urlRegex =
      Regex(
        "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}[-a-zA-Z0-9()@:%_+.~#" +
          "?&/=]+"
      )

    /**
     * Creates a [Link].
     *
     * @param url [URL] to which the [Link] links.
     * @param indices Indices at which both the style symbols and the target are in the whole
     *   [String].
     */
    fun to(url: URL, indices: IntRange): Link {
      return object : Link {
        override val indices = indices
        override val url = url

        override fun equals(other: Any?): Boolean {
          return other is Link && url == other.url && indices == other.indices
        }

        override fun hashCode(): Int {
          return Objects.hash(url, indices)
        }

        override fun toString(): String {
          return "Link(url=$url, indices=$indices)"
        }
      }
    }
  }
}
