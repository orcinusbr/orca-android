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

package com.jeanbarrossilva.orca.core.sample.test.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSamples
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Post] returned by [sample]. */
private val testSamplePost = Post.createSample(TestSampleImageLoader.Provider)

/** [Post]s returned by [samples]. */
private val testSamplePosts = Post.createSamples(TestSampleImageLoader.Provider)

/** Test sample [Post]. */
val Post.Companion.sample
  get() = testSamplePost

/** Test sample [Post]s. */
val Post.Companion.samples
  get() = testSamplePosts
