package com.jeanbarrossilva.orca.core.sample.feed

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.test.SampleTestRule
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootWriter
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule

internal class SampleFeedProviderTests {
    @get:Rule
    val sampleRule = SampleTestRule()

    @Test
    fun `GIVEN an empty list of toots WHEN providing it THEN it's provided`() {
        SampleTootWriter.clear()
        runTest {
            assertContentEquals(
                emptyList(),
                SampleFeedProvider.provide(Profile.sample.id, 0).first()
            )
        }
    }
}
