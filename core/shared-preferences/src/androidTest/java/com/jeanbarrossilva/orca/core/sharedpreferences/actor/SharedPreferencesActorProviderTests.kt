package com.jeanbarrossilva.orca.core.sharedpreferences.actor

import com.jeanbarrossilva.orca.core.sharedpreferences.actor.test.SharedPreferencesCoreTestRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class SharedPreferencesActorProviderTests {
    @get:Rule
    val coreRule = SharedPreferencesCoreTestRule()

    @Test
    fun remembersActorWhenProvidingItForTheFirstTime() {
        runTest {
            val originalActor = coreRule.actorProvider.provide()
            val rememberedActor = coreRule.actorProvider.provide()
            assertEquals(originalActor, rememberedActor)
        }
    }
}
