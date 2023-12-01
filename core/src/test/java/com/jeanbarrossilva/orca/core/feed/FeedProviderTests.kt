/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.core.feed

import app.cash.turbine.test
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.samples
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
          return flowOf(Post.samples)
        }

        override suspend fun containsUser(userID: String): Boolean {
          return true
        }
      }
    runTest { assertContentEquals(Post.samples, provider.provide(userID = "🥸", page = 0).first()) }
  }

  @Test
  fun `GIVEN some muted terms WHEN providing a feed THEN posts with these terms are filtered out`() {
    val termMuter = SampleTermMuter()
    val provider =
      object : FeedProvider() {
        override val termMuter = termMuter

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Post>> {
          return flowOf(Post.samples.take(1))
        }

        override suspend fun containsUser(userID: String): Boolean {
          return true
        }
      }
    runTest {
      Post.samples.first().content.text.split(' ').take(2).forEach { termMuter.mute(it) }
      provider.provide(userID = "😶‍🌫️", page = 0).test {
        assertContentEquals(emptyList(), awaitItem())
        awaitComplete()
      }
    }
  }
}
