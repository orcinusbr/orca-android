/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.platform.autos.kit.sheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.jeanbarrossilva.orca.platform.autos.kit.end
import com.jeanbarrossilva.orca.platform.autos.kit.start

/**
 * Creates [PaddingValues] with whose bounds the given amount of [Dp]s is summed.
 *
 * @param other Amount of [Dp]s to sum with this [PaddingValues]' bounds.
 */
@Composable
internal operator fun PaddingValues.plus(other: Dp): PaddingValues {
  return PaddingValues(
    start + other,
    calculateTopPadding() + other,
    end + other,
    calculateBottomPadding() + other
  )
}
