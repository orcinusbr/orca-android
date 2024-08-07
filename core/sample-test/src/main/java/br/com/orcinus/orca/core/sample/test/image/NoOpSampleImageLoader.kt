/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.sample.test.image

import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.std.image.ImageLoader

/** [ImageLoader] that does not load an image from a [SampleImageSource]. */
class NoOpSampleImageLoader private constructor() : ImageLoader<SampleImageSource, Unit> {
  override val source = SampleImageSource.None

  /** [ImageLoader.Provider] that provides a [NoOpSampleImageLoader]. */
  object Provider : ImageLoader.Provider<SampleImageSource, Unit> {
    override fun provide(source: SampleImageSource): NoOpSampleImageLoader {
      return instance
    }
  }

  override fun load() {}

  companion object {
    /** Single instance of a [NoOpSampleImageLoader]. */
    private val instance = NoOpSampleImageLoader()
  }
}
