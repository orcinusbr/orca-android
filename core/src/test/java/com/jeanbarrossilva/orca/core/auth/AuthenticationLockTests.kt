package com.jeanbarrossilva.orca.core.auth

import com.jeanbarrossilva.orca.core.test.TestActorProvider
import com.jeanbarrossilva.orca.core.test.TestAuthenticationLock
import com.jeanbarrossilva.orca.core.test.TestAuthenticator
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class AuthenticationLockTests {
    @Test
    fun `GIVEN an unauthenticated actor WHEN unlocking THEN it's authenticated`() {
        var hasBeenAuthenticated = false
        val authenticator = TestAuthenticator { hasBeenAuthenticated = true }
        runTest {
            TestAuthenticationLock(authenticator = authenticator).requestUnlock {
            }
        }
        assertTrue(hasBeenAuthenticated)
    }

    @Test
    fun `GIVEN an authenticated actor WHEN unlocking THEN the listener is notified`() {
        val actorProvider = TestActorProvider()
        val authenticator = TestAuthenticator(actorProvider = actorProvider)
        var hasListenerBeenNotified = false
        runTest {
            authenticator.authenticate()
            TestAuthenticationLock(actorProvider, authenticator).requestUnlock {
                hasListenerBeenNotified = true
            }
        }
        assertTrue(hasListenerBeenNotified)
    }
}
