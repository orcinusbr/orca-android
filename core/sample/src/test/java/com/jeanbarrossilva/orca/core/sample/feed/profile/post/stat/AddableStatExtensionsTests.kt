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

package com.jeanbarrossilva.orca.core.sample.feed.profile.post.stat

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.withSample
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class AddableStatExtensionsTests {
  @Test
  fun addsElementToSampleAddableStat() {
    val addableStat = createSampleAddableStat<Post>()
    val post = Posts.withSample.single()
    runTest {
      addableStat.get(page = 0).test {
        awaitItem()
        addableStat.add(post)
        assertThat(awaitItem()).contains(post)
      }
    }
  }

  @Test
  fun removesElementFromSampleAddableStat() {
    val addableStat = createSampleAddableStat<Post>()
    val post = Posts.withSample.single()
    runTest {
      addableStat.get(page = 0).test {
        awaitItem()
        addableStat.add(post)
        awaitItem()
        addableStat.remove(post)
        assertThat(awaitItem()).doesNotContain(post)
      }
    }
  }
}
