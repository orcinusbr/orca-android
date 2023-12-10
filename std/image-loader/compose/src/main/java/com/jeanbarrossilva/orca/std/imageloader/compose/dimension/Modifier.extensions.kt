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

package com.jeanbarrossilva.orca.std.imageloader.compose.dimension

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.isSpecified
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.`if`
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Sizes the content according to the given [size]'s explicit dimensions.
 *
 * @param size Size.
 * @see ImageLoader.Size.Dimension
 * @see ImageLoader.Size.Dimension.Explicit
 */
internal fun Modifier.size(size: ImageLoader.Size): Modifier {
  return composed {
    val density = LocalDensity.current
    val (width, height) = with(density) { size.width.toDp() to size.height.toDp() }
    `if`(width.isSpecified) { width(width) }.`if`(height.isSpecified) { height(height) }
  }
}
