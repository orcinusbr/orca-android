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

/** [Style] for referencing a username. */
data class Mention(override val indices: IntRange, override val url: URL) :
  Style.Constrained(), Link {
  override val regex = Regex("[a-zA-Z0-9._%+-]+")

  companion object
}