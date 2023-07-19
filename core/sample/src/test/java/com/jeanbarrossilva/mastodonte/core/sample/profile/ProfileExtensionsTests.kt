package com.jeanbarrossilva.mastodonte.core.sample.profile

import com.jeanbarrossilva.mastodonte.core.profile.Profile
import com.jeanbarrossilva.mastodonte.core.sample.toot.samples
import com.jeanbarrossilva.mastodonte.core.toot.Toot
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

internal class ProfileExtensionsTests {
    @Test
    fun `GIVEN a sample profile WHEN getting its toots THEN they are the sample ones`() {
        runTest {
            assertContentEquals(
                Toot.samples.take(SampleProfile.TOOTS_PER_PAGE),
                Profile.sample.getToots(0).first()
            )
        }
    }
}
