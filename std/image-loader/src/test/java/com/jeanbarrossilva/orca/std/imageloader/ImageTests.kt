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

package com.jeanbarrossilva.orca.std.imageloader

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import java.awt.Color
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class ImageTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenPixelsAreInsufficient() {
    runTest { buildImage(width = 2, height = 2) { pixel(Color.BLACK.rgb) } }
  }

  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenAddingMorePixelsThanTheSizeCanHold() {
    runTest { buildImage(width = 1, height = 1) { repeat(2) { pixel(Color.BLACK.rgb) } } }
  }

  @Test
  fun addsPixelsToTheCoordinateThatSucceedsThePreviousOne() {
    runTest {
      assertThat(buildImage(width = 2, height = 2) { repeat(4) { pixel(Color.BLACK.rgb) } }.pixels)
        .containsExactly(
          Image.Pixel(x = 0, y = 0, Color.BLACK.rgb),
          Image.Pixel(x = 1, y = 0, Color.BLACK.rgb),
          Image.Pixel(x = 0, y = 1, Color.BLACK.rgb),
          Image.Pixel(x = 1, y = 1, Color.BLACK.rgb)
        )
    }
  }

  @Test
  fun widthIsTheSameAsSpecifiedWhenBuildingTheImage() {
    runTest {
      assertThat(buildImage(width = 2, height = 1) { repeat(2) { pixel(Color.BLACK.rgb) } }.width)
        .isEqualTo(2)
    }
  }

  @Test
  fun heightIsTheSameAsSpecifiedWhenBuildingTheImage() {
    runTest {
      assertThat(buildImage(width = 1, height = 2) { repeat(2) { pixel(Color.BLACK.rgb) } }.height)
        .isEqualTo(2)
    }
  }
}
