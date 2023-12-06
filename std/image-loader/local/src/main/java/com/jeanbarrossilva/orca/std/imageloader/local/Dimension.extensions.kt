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

package com.jeanbarrossilva.orca.std.imageloader.local

import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Returns whether this dimension is explicit.
 *
 * @see ImageLoader.Size.Dimension
 * @see ImageLoader.Size.Dimension.Explicit
 */
@OptIn(ExperimentalContracts::class)
internal fun ImageLoader.Size.Dimension.isExplicit(): Boolean {
  contract { returns() implies (this@isExplicit is ImageLoader.Size.Dimension.Explicit) }
  return this is ImageLoader.Size.Dimension.Explicit
}
