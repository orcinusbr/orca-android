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

package com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * Creates a sample [Highlight].
 *
 * @param coverLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Highlight]'s [headline][Highlight.headline]'s cover will be loaded from [SampleImageSource].
 */
fun Highlight.Companion.createSample(
  coverLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Highlight {
  return Highlight(Headline.createSample(coverLoaderProvider), URL("https://mastodon.social"))
}
