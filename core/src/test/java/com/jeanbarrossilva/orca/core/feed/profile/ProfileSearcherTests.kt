package com.jeanbarrossilva.orca.core.feed.profile

import app.cash.turbine.test
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileSearcher
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

internal class ProfileSearcherTests {
    @Test
    fun `GIVEN a blank query WHEN searching THEN it returns an empty Flow`() {
        runTest {
            SampleProfileSearcher.search(" ").test {
                awaitComplete()
            }
        }
    }
}
