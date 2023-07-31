package com.jeanbarrossilva.mastodonte.core.sample.feed.profile.toot

import com.jeanbarrossilva.mastodonte.core.feed.profile.toot.Toot
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

internal class SampleTootProviderTests {
    @Test
    fun `GIVEN all toot samples WHEN getting them by their IDs THEN they're returned`() {
        runTest {
            Toot.samples.forEach {
                assertEquals(it, SampleTootProvider.provide(it.id).first())
            }
        }
    }
}
