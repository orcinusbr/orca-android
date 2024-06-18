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

@file:JvmName("CornerBasedShapes")

package br.com.orcinus.orca.platform.autos.kit

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.ZeroCornerSize

/** Version of this [CornerBasedShape] with zeroed top [CornerSize]s. */
internal val CornerBasedShape.bottom
  get() = copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize)

/** Version of this [CornerBasedShape] with zeroed bottom [CornerSize]s. */
internal val CornerBasedShape.top
  get() = copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize)
