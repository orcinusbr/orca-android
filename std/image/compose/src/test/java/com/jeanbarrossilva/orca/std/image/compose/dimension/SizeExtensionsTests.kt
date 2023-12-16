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

package com.jeanbarrossilva.orca.std.image.compose.dimension

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.image.ImageLoader
import kotlin.test.Test

internal class SizeExtensionsTests {
  @Test
  fun convertsSizeWithBothDimensionsBeingExplicitIntoSpecifiedDpSize() {
    assertThat(with(Density(1f)) { ImageLoader.Size.explicit(width = 2, height = 1).toDpSize() })
      .isEqualTo(DpSize(width = 2.dp, height = 1.dp))
  }

  @Test
  fun convertsSizeWithExplicitWidthAndAutomaticHeightIntoDpSizeWithSpecifiedWidthAndUnspecifiedHeight() {
    assertThat(with(Density(1f)) { ImageLoader.Size.width(2).toDpSize() })
      .isEqualTo(DpSize(width = 2.dp, height = Dp.Unspecified))
  }

  @Test
  fun convertsSizeWithAutomaticWidthAndExplicitHeightIntoDpSizeWithUnspecifiedWidthAndSpecifiedHeight() {
    assertThat(with(Density(1f)) { ImageLoader.Size.height(1).toDpSize() })
      .isEqualTo(DpSize(width = Dp.Unspecified, height = 1.dp))
  }
}
