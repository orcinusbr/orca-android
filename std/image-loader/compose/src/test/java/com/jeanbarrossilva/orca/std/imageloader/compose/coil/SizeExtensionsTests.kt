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

import assertk.assertThat
import assertk.assertions.isEqualTo
import coil.size.Dimension
import coil.size.Size
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import kotlin.test.Test

internal class SizeExtensionsTests {
  @Test
  fun getsCoilSizeWithAutomaticWidthAndHeightFromExplicitWidthAndHeight() {
    assertThat(ImageLoader.Size.explicit(width = 2, height = 4).coil)
      .isEqualTo(Size(width = 2, height = 4))
  }

  @Test
  fun getsCoilSizeWithPixelsWidthAndUndefinedHeightFromExplicitWidthAndAutomaticHeight() {
    assertThat(ImageLoader.Size.width(2).coil)
      .isEqualTo(Size(width = 2, height = Dimension.Undefined))
  }

  @Test
  fun getsCoilSizeWithUndefinedWidthAndPixelsHeightFromAutomaticWidthAndExplicitHeight() {
    assertThat(ImageLoader.Size.height(2).coil)
      .isEqualTo(Size(width = Dimension.Undefined, height = Dimension.Pixels(2)))
  }
}
