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

package com.jeanbarrossilva.orca.core.sample.instance

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSamples
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.image.Image
import com.jeanbarrossilva.orca.std.image.ImageLoader

/**
 * Creates a [SampleInstance].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s can be loaded from a [SampleImageSource].
 * @param defaultPosts [Post]s that are provided by default within the [SampleInstance].
 */
fun Instance.Companion.createSample(
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>,
  defaultPosts: List<Post> = Post.createSamples(imageLoaderProvider)
): SampleInstance {
  return SampleInstance(defaultPosts, imageLoaderProvider)
}
