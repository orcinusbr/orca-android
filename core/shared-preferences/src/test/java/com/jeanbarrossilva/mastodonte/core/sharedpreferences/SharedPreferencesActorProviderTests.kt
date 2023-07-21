package com.jeanbarrossilva.mastodonte.core.sharedpreferences

import com.jeanbarrossilva.mastodonte.core.test.TestAuthenticator
import com.jeanbarrossilva.mastodonte.core.test.TestAuthorizer
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
        val authorizer = TestAuthorizer()
        val authenticator = TestAuthenticator()
        val actorProvider = SharedPreferencesActorProvider(application, authorizer, authenticator)
        runTest {
            val originalActor = actorProvider.provide()
            val rememberedActor = actorProvider.provide()
            assertEquals(originalActor, rememberedActor)
        }
        actorProvider.reset()
    }
}
