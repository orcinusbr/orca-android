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

package br.com.orcinus.orca.std.styledstring.style.type

import br.com.orcinus.orca.std.styledstring.style.Style
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
