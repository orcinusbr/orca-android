/*
 * Copyright © 2023-2024 Orca
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

package br.com.orcinus.orca.platform.core

import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.feed.profile.post.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.createSamples
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader

/** [Posts] returned by [withSample]. */
private val postsWithSample = Posts {
  add { Post.createSample(ComposableImageLoader.Provider.sample) }
}

/** [Posts] returned by [withSamples]. */
private val postsWithSamples = Posts {
  addAll { Post.createSamples(ComposableImageLoader.Provider.sample) }
}

/** [Posts] whose sample's images are loaded by a sample [ComposableImageLoader]. */
val Posts.Companion.withSample
  get() = postsWithSample

/** Sample [Posts] whose images are loaded by a [ComposableImageLoader]. */
val Posts.Companion.withSamples
  get() = postsWithSamples
