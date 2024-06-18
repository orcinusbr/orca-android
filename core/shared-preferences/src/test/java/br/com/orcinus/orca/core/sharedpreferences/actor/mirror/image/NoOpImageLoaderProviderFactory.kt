/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.sharedpreferences.actor.mirror.image

import br.com.orcinus.orca.core.sample.test.image.TestSampleImageLoader
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI

/**
 * [ImageLoaderProviderFactory] for creating [ImageLoader.Provider]s that provide no-op
 * [ImageLoader]s.
 */
internal object NoOpImageLoaderProviderFactory : ImageLoaderProviderFactory() {
  override fun createForSampleImageSource(): SomeImageLoaderProvider<Any> {
    @Suppress("UNCHECKED_CAST")
    return TestSampleImageLoader.Provider as SomeImageLoaderProvider<Any>
  }

  override fun createForURI(): SomeImageLoaderProvider<URI> {
    return ImageLoader.Provider {
      object : ImageLoader<URI, Unit> {
        override val source = it

        override fun load() {}
      }
    }
  }
}
