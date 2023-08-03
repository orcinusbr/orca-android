package com.jeanbarrossilva.orca.core.feed.profile.search

import com.jeanbarrossilva.orca.core.sample.feed.profile.search.SampleProfileSearcher
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

internal class ProfileSearcherTests {
    @Test
    fun `GIVEN a blank query WHEN searching THEN it returns no results`() {
        runTest {
            assertContentEquals(emptyList(), SampleProfileSearcher.search(" ").first())
        }
    }
}
