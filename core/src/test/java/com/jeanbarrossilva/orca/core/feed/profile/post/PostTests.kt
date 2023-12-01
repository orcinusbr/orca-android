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

package com.jeanbarrossilva.orca.core.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.test.TestPost
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class PostTests {
  @Test
  fun `GIVEN an unliked post WHEN liking it THEN it's liked`() {
    val post = TestPost()
    runTest {
      post.favorite.disable()
      post.favorite.enable()
      assertTrue(post.favorite.isEnabled)
    }
  }

  @Test
  fun `GIVEN a liked post WHEN unliking it THEN it isn't liked`() {
    val post = TestPost()
    runTest {
      post.favorite.enable()
      post.favorite.disable()
      assertFalse(post.favorite.isEnabled)
    }
  }

  @Test
  fun `GIVEN a post WHEN reposting it THEN it's reposted`() {
    val post = TestPost()
    runTest {
      post.repost.disable()
      post.repost.enable()
      assertTrue(post.repost.isEnabled)
    }
  }

  @Test
  fun `GIVEN a repost WHEN unreposting it THEN it isn't reposted`() {
    val post = TestPost()
    runTest {
      post.repost.enable()
      post.repost.disable()
      assertFalse(post.repost.isEnabled)
    }
  }
}
