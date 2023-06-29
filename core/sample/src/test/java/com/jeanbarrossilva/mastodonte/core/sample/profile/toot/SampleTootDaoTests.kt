package com.jeanbarrossilva.mastodonte.core.sample.profile.toot

import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

internal class SampleTootDaoTests {
    @Test
    fun `GIVEN all toot samples WHEN getting them by their IDs THEN they're returned`() {
        runTest {
            Toot.samples.forEach {
                assertEquals(it, SampleTootDao.get(it.id).first())
            }
        }
    }
}
