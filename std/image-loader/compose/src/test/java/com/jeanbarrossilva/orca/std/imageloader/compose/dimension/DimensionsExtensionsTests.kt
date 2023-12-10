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

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import kotlin.test.Test

internal class DimensionsExtensionsTests {
  @Test
  fun convertsAutomaticDimensionIntoUnspecifiedDP() {
    assertThat(with(Density(1f)) { ImageLoader.Size.Dimension.Automatic.toDp() })
      .isEqualTo(Dp.Unspecified)
  }

  @Test
  fun convertsExplicitDimensionIntoSpecifiedDPs() {
    assertThat(with(Density(1f)) { ImageLoader.Size.Dimension.Explicit(2).toDp() }).isEqualTo(2.dp)
  }
}
