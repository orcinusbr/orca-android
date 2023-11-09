package com.jeanbarrossilva.orca.std.styledstring.type

import com.jeanbarrossilva.orca.std.styledstring.Style
import java.net.URL
import java.util.Objects

/**
 * [Style] for [URL]s.
 *
 * @param url [URL] to which this [Link] links.
 */
abstract class Link internal constructor() : Style() {
  /** [URL] to which this [Link] links. */
  abstract val url: URL

  /** [Style.Delimiter] for [Link]s. */
  sealed class Delimiter : Style.Delimiter() {
    /**
     * [Delimiter] that's the [parent] of all [Variant]s and considers [Link]s parts of a [String]
     * conforming to a [URL] format.
     */
    internal object Default : Delimiter() {
      override val parent = null

      override fun getRegex(): Regex {
        return protocoledRegex
      }

      override fun onGetTarget(match: String): String {
        return match
      }

      override fun onTarget(target: String): String {
        return target
      }
    }

    /** [Delimiter] that's a child of [Default]. */
    abstract class Variant : Delimiter() {
      final override val parent = Default
    }
  }

  override fun equals(other: Any?): Boolean {
    return other is Link && url == other.url && indices == other.indices
  }

  override fun hashCode(): Int {
    return Objects.hash(url, indices)
  }

  override fun toString(): String {
    return "Link(url=$url, indices=$indices)"
  }

  companion object {
    /** [Regex] that matches the [protocol][URL.getProtocol] of a [URL]. */
    val protocolRegex = Regex("https?://")

    /** [Regex] that matches the [path][URL.path] of a [URL]. */
    val pathRegex = Regex("[-a-zA-Z0-9()@:%_+.~#?&/=]+")

    /** [Regex] that matches the subdomain of a URL. */
    val subdomainRegex = Regex("www\\.")

    /** [Regex] that matches [String]s with an unprotocoled [URL] format. */
    val unprotocoledRegex =
      Regex("($subdomainRegex)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}$pathRegex")

    /** [Regex] that matches [String]s with a [URL] format. */
    val protocoledRegex = Regex("$protocolRegex" + "$unprotocoledRegex")

    /**
     * Creates a [Link].
     *
     * @param url [URL] to which the [Link] links.
     * @param indices Indices at which both the style symbols and the target are in the whole
     *   [String].
     */
    fun to(url: URL, indices: IntRange): Link {
      return object : Link() {
        override val indices = indices
        override val url = url
      }
    }
  }
}
