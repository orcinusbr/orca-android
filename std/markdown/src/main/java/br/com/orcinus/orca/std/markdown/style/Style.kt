/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.std.markdown.style

import java.io.Serializable
import java.net.URL
import java.util.Objects

/** Indicates where and how a target has been stylized. */
interface Style : Serializable {
  /** Indices at which both the style symbols and the target are in the whole [String]. */
  val indices: IntRange

  /** [Style] that requires its targets to match the [regex]. */
  abstract class Constrained protected constructor() : Style {
    /** [Regex] that targets to which this [Style] is applied should match. */
    internal abstract val regex: Regex

    /**
     * [IllegalArgumentException] thrown if the target is an invalid one for this [Style].
     *
     * @param target Target that's invalid.
     */
    inner class InvalidTargetException internal constructor(target: String) :
      IllegalArgumentException("Target doesn't match regex ($regex): \"$target\".")
  }

  /** [Style] that applies a heavier weight to the target's font. */
  data class Bold(override val indices: IntRange) : Style {
    override fun at(indices: IntRange): Bold {
      return copy(indices = indices)
    }
  }

  /** [Style] for referencing an e-mail, such as "john@appleseed.com". */
  data class Email(override val indices: IntRange) : Style {
    override fun at(indices: IntRange): Email {
      return copy(indices = indices)
    }

    companion object {
      /** [Regex] that matches an [Email]. */
      val regex =
        Regex(
          "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x0" +
            "8\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-" +
            "\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])" +
            "?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[" +
            "0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-" +
            "\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])"
        )
    }
  }

  /** [Style] for a specific subject. */
  data class Hashtag(override val indices: IntRange) : Constrained() {
    override val regex = Regex("\\w+")

    override fun at(indices: IntRange): Hashtag {
      return copy(indices = indices)
    }
  }

  /** [Style] that slightly slants the target [String] to the right. */
  data class Italic(override val indices: IntRange) : Style {
    override fun at(indices: IntRange): Style {
      return copy(indices = indices)
    }
  }

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
          "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}[-a-zA-Z0-9()@:%_+." +
            "~#?&/=]+"
        )

      /**
       * Creates a [Link].
       *
       * @param url [URL] to which the [Link] links.
       * @param indices Indices at which both the style symbols and the target are in the whole
       *   [String].
       */
      @JvmStatic
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

  /** [Style] for referencing a username. */
  data class Mention(override val indices: IntRange, override val url: URL) : Constrained(), Link {
    override val regex = Regex("[a-zA-Z0-9._%+-]+")

    companion object
  }

  /**
   * Copies this [Style].
   *
   * @param indices Indices at which both the style symbols and the target are in the whole
   *   [String].
   */
  fun at(indices: IntRange): Style

  companion object {
    /** [Style] whose [indices] are empty. */
    val empty =
      object : Style {
        override val indices = IntRange.EMPTY

        override fun at(indices: IntRange): Style {
          return this
        }
      }
  }
}
