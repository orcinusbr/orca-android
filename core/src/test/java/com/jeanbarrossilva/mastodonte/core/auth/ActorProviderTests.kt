package com.jeanbarrossilva.mastodonte.core.auth

import com.jeanbarrossilva.mastodonte.core.auth.test.TestActorProvider
import com.jeanbarrossilva.mastodonte.core.auth.test.TestAuthenticator
import com.jeanbarrossilva.mastodonte.core.auth.test.TestAuthorizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest

internal class ActorProviderTests {
    @Test
    fun `GIVEN an unauthenticated actor WHEN providing THEN it throws`() {
        assertFailsWith<ActorProvider.UnauthenticatedException> {
            TestActorProvider().provide()
        }
    }

    @Test
    fun `GIVEN an authenticated actor WHEN providing THEN it equals to the authenticator's`() {
        val authorizer = TestAuthorizer()
        val authenticator = TestAuthenticator()
        val actorProvider = TestActorProvider(authenticator)
        runTest {
            val actor = authenticator.authenticate(authorizer)
            assertEquals(actor, actorProvider.provide())
        }
    }
}
