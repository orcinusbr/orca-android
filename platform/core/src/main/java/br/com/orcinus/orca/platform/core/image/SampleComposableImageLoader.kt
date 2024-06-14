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

package br.com.orcinus.orca.platform.core.image

import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.compose.ComposableImage
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import java.util.Objects

/** [ComposableImageLoader] that loads images from a [SampleImageSource]. */
internal class SampleComposableImageLoader
private constructor(override val source: SampleImageSource) :
  ComposableImageLoader<SampleImageSource>() {
  /** [ImageLoader.Provider] that provides a [SampleComposableImageLoader]. */
  object Provider : ComposableImageLoader.Provider<SampleImageSource> {
    override fun provide(source: SampleImageSource): SampleComposableImageLoader {
      return SampleComposableImageLoader(source)
    }
  }

  override fun equals(other: Any?): Boolean {
    return other is SampleComposableImageLoader && source == other.source
  }

  override fun hashCode(): Int {
    return Objects.hash(source)
  }

  override fun load(): ComposableImage {
    return { contentDescription, shape, contentScale, modifier ->
      Image(
        painterResource(source.resourceID),
        contentDescription,
        modifier.clip(shape),
        contentScale = contentScale
      )
    }
  }
}
