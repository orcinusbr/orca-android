package com.jeanbarrossilva.mastodonte.core.auth

import com.jeanbarrossilva.mastodonte.core.test.TestActorProvider
import com.jeanbarrossilva.mastodonte.core.test.TestAuthenticationLock
import com.jeanbarrossilva.mastodonte.core.test.TestAuthenticator
import com.jeanbarrossilva.mastodonte.core.test.TestAuthorizer
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class AuthenticationLockTests {
    @Test
    fun `GIVEN an unauthenticated actor WHEN locking THEN the listener is notified`() {
        var hasListenerBeenNotified = false
        runTest {
            TestAuthenticationLock().lock {
                hasListenerBeenNotified = true
            }
        }
        assertTrue(hasListenerBeenNotified)
    }

    @Test
    fun `GIVEN an authenticated actor WHEN locking THEN the listener isn't notified`() {
        val authorizer = TestAuthorizer()
        val actorProvider = TestActorProvider()
        val authenticator = TestAuthenticator(actorProvider)
        var hasListenerBeenNotified = false
        runTest {
            authenticator.authenticate(authorizer)
            TestAuthenticationLock(authorizer, actorProvider, authenticator).lock {
                hasListenerBeenNotified = true
            }
        }
        assertFalse(hasListenerBeenNotified)
    }

    @Test
    fun `GIVEN an unauthenticated actor WHEN unlocking THEN it's authenticated`() {
        var hasBeenAuthenticated = false
        val authenticator = TestAuthenticator { hasBeenAuthenticated = true }
        runTest {
            TestAuthenticationLock(authenticator = authenticator).unlock {
            }
        }
        assertTrue(hasBeenAuthenticated)
    }

    @Test
    fun `GIVEN an authenticated actor WHEN unlocking THEN the listener is notified`() {
        var hasListenerBeenNotified = false
        runTest {
            TestAuthenticationLock().lock {
                hasListenerBeenNotified = true
            }
        }
        assertTrue(hasListenerBeenNotified)
    }
}
