package com.jeanbarrossilva.mastodonte.core.auth

import com.jeanbarrossilva.mastodonte.core.auth.test.TestActorProvider
import com.jeanbarrossilva.mastodonte.core.auth.test.TestAuthenticator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class ActorProviderTests {
    @Test
    fun `GIVEN a remembered unauthenticated actor WHEN providing THEN it authenticates`() {
        var hasAuthenticated = false
        val authenticator = TestAuthenticator { hasAuthenticated = true }
        runTest {
            TestActorProvider(authenticator = authenticator).provide()
            assertTrue(hasAuthenticated)
        }
    }

    @Test
    fun `GIVEN a remembered authenticated actor WHEN providing THEN it's the one that's provided`() { // ktlint-disable max-line-length
        val authenticator = TestAuthenticator()
        val actorProvider = TestActorProvider(authenticator = authenticator)
        runTest {
            actorProvider.provide()
            assertEquals(actorProvider.rememberedActor, actorProvider.provide())
        }
    }
}
