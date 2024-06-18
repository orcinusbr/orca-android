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

package br.com.orcinus.orca.core.sample.feed.profile.post.content.highlight

import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Headline
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Highlight
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider

/**
 * Creates a sample [Highlight].
 *
 * @param coverLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Highlight]'s [headline][Highlight.headline]'s cover will be loaded from [SampleImageSource].
 */
fun Highlight.Companion.createSample(
  coverLoaderProvider: SomeImageLoaderProvider<SampleImageSource>
): Highlight {
  return Highlight(
    Headline.createSample(coverLoaderProvider),
    URIBuilder.url().scheme("https").host("pixelpals.app").build()
  )
}
