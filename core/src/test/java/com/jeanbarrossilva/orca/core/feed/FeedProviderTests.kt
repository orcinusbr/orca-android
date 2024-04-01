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

package com.jeanbarrossilva.orca.core.feed

import app.cash.turbine.test
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.withSamples
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class FeedProviderTests {
  @Test
  fun `GIVEN a nonexistent user's ID WHEN requesting a feed to be provided with it THEN it throws`() {
    val provider =
      object : FeedProvider() {
        override val termMuter = SampleTermMuter()

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Post>> {
          return emptyFlow()
        }

        override suspend fun containsUser(userID: String): Boolean {
          return false
        }
      }
    assertFailsWith<FeedProvider.NonexistentUserException> {
      runTest { provider.provide(userID = "🫨", page = 0) }
    }
  }

  @Test
  fun `GIVEN a negative page WHEN requesting a feed to be provided with it THEN it throws`() {
    val provider =
      object : FeedProvider() {
        override val termMuter = SampleTermMuter()

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Post>> {
          return emptyFlow()
        }

        override suspend fun containsUser(userID: String): Boolean {
          return true
        }
      }
    assertFailsWith<IndexOutOfBoundsException> {
      runTest { provider.provide(userID = "🥲", page = -1) }
    }
  }

  @Test
  fun `GIVEN a user ID WHEN requesting a feed to be provided with it THEN it's provided`() {
    val provider =
      object : FeedProvider() {
        override val termMuter = SampleTermMuter()

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Post>> {
          return flowOf(Posts.withSamples)
        }

        override suspend fun containsUser(userID: String): Boolean {
          return true
        }
      }
    runTest {
      assertContentEquals(Posts.withSamples, provider.provide(userID = "🥸", page = 0).first())
    }
  }

  @Test
  fun `GIVEN some muted terms WHEN providing a feed THEN posts with these terms are filtered out`() {
    val termMuter = SampleTermMuter()
    val provider =
      object : FeedProvider() {
        override val termMuter = termMuter

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Post>> {
          return flowOf(Posts.withSamples.take(1))
        }

        override suspend fun containsUser(userID: String): Boolean {
          return true
        }
      }
    runTest {
      Posts.withSamples.first().content.text.split(' ').take(2).forEach { termMuter.mute(it) }
      provider.provide(userID = "😶‍🌫️", page = 0).test {
        assertContentEquals(emptyList(), awaitItem())
        awaitComplete()
      }
    }
  }
}
