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

package com.jeanbarrossilva.orca.std.imageloader.compose

import androidx.compose.ui.unit.Constraints
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import kotlin.test.Test

internal class SizingTests {
  @Test
  fun widthIsMaximizedAndHeightIsAutomaticWhenWideningAndConstraintHeightIsUnbounded() {
    assertThat(
        Sizing.Widened.size(
          Constraints(minWidth = 0, maxWidth = 2, minHeight = 0, maxHeight = Constraints.Infinity)
        )
      )
      .isEqualTo(ImageLoader.Size.width(2))
  }

  @Test
  fun sizeIsAutomaticWhenWideningAndConstraintsAreUnbounded() {
    assertThat(
        Sizing.Widened.size(
          Constraints(
            minWidth = 0,
            maxWidth = Constraints.Infinity,
            minHeight = 0,
            maxHeight = Constraints.Infinity
          )
        )
      )
      .isEqualTo(ImageLoader.Size.automatic)
  }

  @Test
  fun widthIsAutomaticAndHeightIsMaximizedWhenElongatingAndConstraintWidthIsUnbounded() {
    assertThat(
        Sizing.Elongated.size(
          Constraints(minWidth = 0, maxWidth = Constraints.Infinity, minHeight = 0, maxHeight = 2)
        )
      )
      .isEqualTo(ImageLoader.Size.height(2))
  }

  @Test
  fun sizeIsAutomaticWhenElongatingAndConstraintsAreUnbounded() {
    assertThat(
        Sizing.Elongated.size(
          Constraints(
            minWidth = 0,
            maxWidth = Constraints.Infinity,
            minHeight = 0,
            maxHeight = Constraints.Infinity
          )
        )
      )
      .isEqualTo(ImageLoader.Size.automatic)
  }
}
