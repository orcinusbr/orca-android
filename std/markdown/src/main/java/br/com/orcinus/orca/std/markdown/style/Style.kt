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
import java.net.URI

/** Indicates where and how a target has been stylized. */
sealed interface Style : Serializable {
  /** Indices at which both the style symbols and the target are in the whole [String]. */
  val indices: IntRange

  /** [Style] that applies a heavier weight to the target's font. */
  data class Bold(override val indices: IntRange) : Style {
    override fun at(indices: IntRange): Bold {
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
   * [Style] that attaches a [URI] to a text.
   *
   * @param uri [URI] to which this [Link] links.
   */
  data class Link(val uri: URI, override val indices: IntRange) : Style {
    override fun at(indices: IntRange): Style {
      return Link(uri, indices)
    }
  }

  /**
   * Copies this [Style].
   *
   * @param indices Indices at which both the style symbols and the target are in the whole
   *   [String].
   */
  fun at(indices: IntRange): Style
}
