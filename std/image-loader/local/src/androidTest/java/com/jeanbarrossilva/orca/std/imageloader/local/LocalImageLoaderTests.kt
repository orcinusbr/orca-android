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

package com.jeanbarrossilva.orca.std.imageloader.local

import android.graphics.Color
import androidx.test.platform.app.InstrumentationRegistry
import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class LocalImageLoaderTests {
  @Test
  fun loads() {
    val imageLoader =
      object : LocalImageLoader() {
        override val context = InstrumentationRegistry.getInstrumentation().context
        override val source = R.drawable.ic_white
      }
    runTest {
      assertThat(imageLoader.load(ImageLoader.Size.explicit(1, 1))?.pixels.orEmpty()).all {
        hasSize(1)
        given { assertThat(it.single().x).isEqualTo(0) }
        given { assertThat(it.single().y).isEqualTo(0) }
        given { assertThat(it.single().color).isEqualTo(Color.WHITE) }
      }
    }
  }
}
