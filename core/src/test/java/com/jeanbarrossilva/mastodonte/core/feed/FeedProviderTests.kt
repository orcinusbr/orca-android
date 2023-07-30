package com.jeanbarrossilva.mastodonte.core.feed

import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.core.sample.profile.toot.samples
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
    fun `GIVEN a nonexistent user's ID WHEN requesting a feed to be provided with it THEN it throws`() { // ktlint-disable max-line-length
        val provider = object : FeedProvider() {
            override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
                return emptyFlow()
            }

            override suspend fun containsUser(userID: String): Boolean {
                return false
            }
        }
        assertFailsWith<FeedProvider.NonexistentUserException> {
            runTest {
                provider.provide(userID = "🫨", page = 0)
            }
        }
    }

    @Test
    fun `GIVEN a negative page WHEN requesting a feed to be provided with it THEN it throws`() {
        val provider = object : FeedProvider() {
            override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
                return emptyFlow()
            }

            override suspend fun containsUser(userID: String): Boolean {
                return true
            }
        }
        assertFailsWith<IndexOutOfBoundsException> {
            runTest {
                provider.provide(userID = "🥲", page = -1)
            }
        }
    }

    @Test
    fun `GIVEN a user ID WHEN requesting a feed to be provided with it THEN it's provided`() {
        val provider = object : FeedProvider() {
            override suspend fun onProvide(userID: String, page: Int): Flow<List<Toot>> {
                return flowOf(Toot.samples)
            }

            override suspend fun containsUser(userID: String): Boolean {
                return true
            }
        }
        runTest {
            assertContentEquals(Toot.samples, provider.provide(userID = "🥸", page = 0).first())
        }
    }
}
