/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.std.imageloader.compose.coil

import coil.size.Dimension
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/** Coil's [Dimension] version of this [ImageLoader.Size.Dimension]. */
internal val ImageLoader.Size.Dimension.coil
  get() =
    when (this) {
      is ImageLoader.Size.Dimension.Automatic -> Dimension.Undefined
      is ImageLoader.Size.Dimension.Explicit -> Dimension.Pixels(value)
    }
