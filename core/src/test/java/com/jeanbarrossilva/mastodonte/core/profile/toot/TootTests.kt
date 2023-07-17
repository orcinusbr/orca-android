package com.jeanbarrossilva.mastodonte.core.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.test.TestToot
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class TootTests {
    @Test
    fun `GIVEN an unliked toot WHEN liking it THEN it's liked`() {
        val toot = TestToot(isLiked = false)
        runTest {
            toot.toggleFavorite()
            assertTrue(toot.isFavorite)
        }
    }

    @Test
    fun `GIVEN a liked toot WHEN unliking it THEN it isn't liked`() {
        val toot = TestToot(isLiked = true)
        runTest {
            toot.toggleFavorite()
            assertFalse(toot.isFavorite)
        }
    }

    @Test
    fun `GIVEN a non-reblogged toot WHEN reblogging it THEN it's reblogged`() {
        val toot = TestToot(isReblogged = false)
        runTest {
            toot.toggleReblogged()
            assertTrue(toot.isReblogged)
        }
    }

    @Test
    fun `GIVEN a reblogged toot WHEN un-reblogging it THEN it isn't reblogged`() {
        val toot = TestToot(isReblogged = true)
        runTest {
            toot.toggleReblogged()
            assertFalse(toot.isReblogged)
        }
    }
}
