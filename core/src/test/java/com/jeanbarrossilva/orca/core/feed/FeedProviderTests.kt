package com.jeanbarrossilva.orca.core.feed

import app.cash.turbine.test
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.muting.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.samples
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

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
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

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
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

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
          return flowOf(Toot.samples)
        }

        override suspend fun containsUser(userID: String): Boolean {
          return true
        }
      }
    runTest { assertContentEquals(Toot.samples, provider.provide(userID = "🥸", page = 0).first()) }
  }

  @Test
  fun `GIVEN some muted terms WHEN providing a feed THEN toots with these terms are filtered out`() {
    val termMuter = SampleTermMuter()
    val provider =
      object : FeedProvider() {
        override val termMuter = termMuter

        override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
          return flowOf(Toot.samples.take(1))
        }

        override suspend fun containsUser(userID: String): Boolean {
          return true
        }
      }
    runTest {
      Toot.samples.first().content.text.split(' ').take(2).forEach { termMuter.mute(it) }
      provider.provide(userID = "😶‍🌫️", page = 0).test {
        assertContentEquals(emptyList(), awaitItem())
        awaitComplete()
      }
    }
  }
}
