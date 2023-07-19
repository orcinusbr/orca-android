package com.jeanbarrossilva.mastodonte.core.auth

import com.jeanbarrossilva.mastodonte.core.auth.test.TestAuthenticator
import com.jeanbarrossilva.mastodonte.core.auth.test.TestAuthorizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class AuthenticatorTests {
    @Test
    fun `GIVEN an authenticator WHEN authenticating with it THEN the user is authorized`() {
        var isAuthorized = false
        val authorizer = TestAuthorizer { isAuthorized = true }
        runTest { TestAuthenticator().authenticate(authorizer) }
        assertTrue(isAuthorized)
    }

    @Test
    fun `GIVEN an authenticator WHEN authenticating with it THEN the authorization code is the same that's been provided by the authorizer`() { // ktlint-disable max-line-length
        lateinit var providedAuthorizationCode: String
        val authorizer = TestAuthorizer()
        val authenticator = TestAuthenticator { providedAuthorizationCode = it }
        runTest { authenticator.authenticate(authorizer) }
        assertEquals(TestAuthorizer.AUTHORIZATION_CODE, providedAuthorizationCode)
    }
}
