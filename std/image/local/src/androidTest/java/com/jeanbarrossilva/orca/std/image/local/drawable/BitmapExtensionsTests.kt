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

package com.jeanbarrossilva.orca.std.image.local.drawable

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.createBitmap
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.isSameAs
import assertk.assertions.prop
import org.junit.Test

internal class BitmapExtensionsTests {
  @Test
  fun returnsReceiverBitmapWhenSamplingItToItsOwnDimensions() {
    val bitmap = createBitmap(width = 1, height = 1)
    assertThat(bitmap.sample(sampledWidth = 1, sampledHeight = 1)).isSameAs(bitmap)
  }

  @Test
  fun samplesWhenDimensionsAreDifferent() {
    assertThat(createBitmap(width = 2, height = 2).sample(sampledWidth = 1, sampledHeight = 1))
      .all {
        prop(Bitmap::getWidth).isEqualTo(1)
        prop(Bitmap::getHeight).isEqualTo(1)
      }
  }

  @Test
  fun sampledBitmapIsNotStoredInGraphicMemory() {
    val byteArray = createBitmap(width = 1, height = 1).toByteArray()
    val options =
      BitmapFactory.Options().apply {
        inMutable = true
        outConfig = Bitmap.Config.HARDWARE
      }
    assertThat(
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
          .sample(sampledWidth = 1, sampledHeight = 1)
          .config
      )
      .isNotEqualTo(Bitmap.Config.HARDWARE)
  }

  @Test
  fun copiesBitmapStoredInGraphicMemoryIntoOneThatIsNot() {
    val byteArray = createBitmap(width = 1, height = 1).toByteArray()
    val options =
      BitmapFactory.Options().apply {
        inMutable = true
        outConfig = Bitmap.Config.HARDWARE
      }
    assertThat(
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
          .withoutHardwareConfiguration()
          .config
      )
      .isNotEqualTo(Bitmap.Config.HARDWARE)
  }

  @Test
  fun returnsReceiverBitmapWhenItIsNotStoredInGraphicMemoryAndIsRequestedToNotBe() {
    val bitmap = createBitmap(width = 1, height = 1, Bitmap.Config.ARGB_8888)
    assertThat(bitmap.withoutHardwareConfiguration()).isSameAs(bitmap)
  }
}
