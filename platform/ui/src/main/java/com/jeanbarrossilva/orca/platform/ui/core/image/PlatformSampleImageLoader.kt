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

package com.jeanbarrossilva.orca.platform.ui.core.image

import android.content.Context
import androidx.core.content.ContextCompat
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.image.Image
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.local.drawable.toImage

internal class PlatformSampleImageLoader
private constructor(private val context: Context, override val source: SampleImageSource) :
  ImageLoader<SampleImageSource> {
  class Provider(private val context: Context) : ImageLoader.Provider<SampleImageSource> {
    override fun provide(source: SampleImageSource): PlatformSampleImageLoader {
      return PlatformSampleImageLoader(context, source)
    }
  }

  override suspend fun load(size: ImageLoader.Size): Image? {
    return ContextCompat.getDrawable(context, source.resourceID)?.toImage(size)
  }
}
