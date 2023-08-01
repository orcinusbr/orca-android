package com.jeanbarrossilva.mastodonte.core.sample.feed.profile

import com.jeanbarrossilva.mastodonte.core.feed.profile.Profile
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.test.SampleTestRule
import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.toot.SampleTootWriter
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule

internal class SampleProfileProviderTests {
    @get:Rule
    val sampleRule = SampleTestRule()

    @Test
    fun `GIVEN a profile without toots WHEN providing it and getting them THEN they're obtained`() {
        SampleTootWriter.clear()
        runTest {
            assertContentEquals(
                emptyList(),
                SampleProfileProvider.provide(Profile.sample.id).first().getToots(0).first()
            )
        }
    }
}
