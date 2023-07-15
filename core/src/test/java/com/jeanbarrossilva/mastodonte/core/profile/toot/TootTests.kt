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
}
