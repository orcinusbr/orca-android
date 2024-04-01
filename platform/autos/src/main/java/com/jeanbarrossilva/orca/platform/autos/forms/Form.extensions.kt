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

@file:JvmName("Forms")

package com.jeanbarrossilva.orca.platform.autos.forms

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import br.com.orcinus.orca.autos.forms.Form

/** [Shape] version of this [Form]. */
val Form.asShape: CornerBasedShape
  get() =
    when (this) {
      is Form.PerCorner -> RoundedCornerShape(topStart.dp, topEnd.dp, bottomEnd.dp, bottomStart.dp)
      is Form.Percent -> RoundedCornerShape(percentage * 100)
    }
