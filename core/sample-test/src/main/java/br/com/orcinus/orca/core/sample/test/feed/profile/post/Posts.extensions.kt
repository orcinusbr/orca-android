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

package br.com.orcinus.orca.core.sample.test.feed.profile.post

import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.feed.profile.post.createSample
import br.com.orcinus.orca.core.sample.feed.profile.post.createSamples
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader

/** [Posts] returned by [withSample]. */
private val testPostsWithSample = Posts {
  add { Post.createSample(NoOpSampleImageLoader.Provider) }
}

/** [Posts] returned by [withSamples]. */
private val testPostsWithSamples = Posts {
  addAll { Post.createSamples(NoOpSampleImageLoader.Provider) }
}

/** Test [Posts] with a sample. */
val Posts.Companion.withSample
  get() = testPostsWithSample

/** Test [Posts] with samples. */
val Posts.Companion.withSamples
  get() = testPostsWithSamples
