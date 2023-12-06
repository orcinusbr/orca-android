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

package com.jeanbarrossilva.orca.core.sample.test.image

import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.buildImage

/** [ImageLoader] that loads an empty [Image] from a [SampleImageSource]. */
class TestSampleImageLoader private constructor() : ImageLoader<SampleImageSource> {
  override val source = SampleImageSource.None

  /** [ImageLoader.Provider] that provides a [TestSampleImageLoader]. */
  object Provider : ImageLoader.Provider<SampleImageSource> {
    override fun provide(source: SampleImageSource): TestSampleImageLoader {
      return instance
    }
  }

  override suspend fun load(size: ImageLoader.Size): Image {
    return buildImage(width = 1, height = 1) { pixel(0) }
  }

  companion object {
    /** Single instance of a [TestSampleImageLoader]. */
    private val instance = TestSampleImageLoader()
  }
}
