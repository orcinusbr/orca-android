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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.size

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText

/**
 * Range within which an [AutoSizeText] should be sized.
 *
 * @param density [Density] through which sizes will be converted from [SP][TextUnitType.Sp] to
 *   [Float] and vice-versa.
 * @param min Minimum size.
 * @param max Maximum, default size.
 */
@Immutable
data class AutoSizeRange
internal constructor(
  private val density: Density,
  private val min: TextUnit,
  private val max: TextUnit
) : ClosedFloatingPointRange<Float> {
  override val start = with(density) { min.toPx() }
  override val endInclusive = with(density) { max.toPx() }

  init {
    require(min.type == max.type) {
      "Both minimum and maximum sizes should have the same TextUnitType."
    }
  }

  override fun lessThanOrEquals(a: Float, b: Float): Boolean {
    return with(density) { a.toSp() <= b.toSp() }
  }
}
