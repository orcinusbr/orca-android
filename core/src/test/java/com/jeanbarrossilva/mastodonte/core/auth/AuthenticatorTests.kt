package com.jeanbarrossilva.mastodonte.core.auth

import com.jeanbarrossilva.mastodonte.core.test.TestAuthenticator
import com.jeanbarrossilva.mastodonte.core.test.TestAuthorizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class AuthenticatorTests {
    @Test
    fun `GIVEN an authentication WHEN verifying if the actor is authorized THEN it is`() {
        var isAuthorized = false
        val authorizer = TestAuthorizer { isAuthorized = true }
        runTest { TestAuthenticator().authenticate(authorizer) }
        assertTrue(isAuthorized)
    }

    @Test
    fun `GIVEN an authentication WHEN comparing the authorization code provided by the authorizer and the one the authenticator receives THEN they're the same`() { // ktlint-disable max-line-length
        lateinit var providedAuthorizationCode: String
        val authorizer = TestAuthorizer()
        val authenticator = TestAuthenticator { providedAuthorizationCode = it }
        runTest { authenticator.authenticate(authorizer) }
        assertEquals(TestAuthorizer.AUTHORIZATION_CODE, providedAuthorizationCode)
    }

    @Test
    fun `GIVEN an authentication WHEN getting the resulting actor THEN it's authenticated`() {
        val authorizer = TestAuthorizer()
        val authenticator = TestAuthenticator()
        runTest {
            assertIs<Actor.Authenticated>(authenticator.authenticate(authorizer))
        }
    }
}
