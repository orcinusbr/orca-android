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

package com.jeanbarrossilva.orca.std.imageloader.local.drawable

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.graphics.drawable.toDrawable
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.buildImage
import com.jeanbarrossilva.orca.std.imageloader.local.toBitmap
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class DrawableExtensionsTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsOnNonBitmapDrawableWhenConvertingItIntoImage() {
    runTest {
      ColorDrawable(Color.TRANSPARENT).toImage(ImageLoader.Size.explicit(width = 1, height = 1))
    }
  }

  @Test
  fun convertsDrawableIntoImage() {
    runTest {
      assertThat(
          buildImage(width = 1, height = 2) {
              pixel(Color.BLACK)
              pixel(Color.WHITE)
            }
            .toBitmap()
            .toDrawable(InstrumentationRegistry.getInstrumentation().context.resources)
            .toImage(ImageLoader.Size.automatic)
            .pixels
        )
        .isEqualTo(
          buildImage(width = 1, height = 2) {
              pixel(Color.BLACK)
              pixel(Color.WHITE)
            }
            .pixels
        )
    }
  }
}
