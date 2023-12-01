/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
