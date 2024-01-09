/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.core.sample.feed.profile.post

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.containsSubList
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.withSample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.withSamples
import kotlin.test.Test

internal class PostsBuilderTests {
  @Test
  fun addsOnePost() {
    val post = Posts.withSample.single()
    assertThat(Posts { add { post } }).containsOnly(post)
  }

  @Test
  fun addsMultiplePosts() {
    val posts = Posts.withSamples
    assertThat(Posts { addAll { posts } }).containsSubList(posts)
  }
}
