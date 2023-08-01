package com.jeanbarrossilva.orca.core.sharedpreferences.actor

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
internal class SharedPreferencesActorProviderTests {
    @Test
    fun `GIVEN a remembered actor WHEN retrieving it THEN it's the same as the original one`() { // ktlint-disable max-line-length
        val application = RuntimeEnvironment.getApplication()
        val actorProvider = SharedPreferencesActorProvider(application)
        runTest {
            val originalActor = actorProvider.provide()
            val rememberedActor = actorProvider.provide()
            assertEquals(originalActor, rememberedActor)
        }
        actorProvider.reset()
    }
}
