package com.jeanbarrossilva.mastodonte.core.auth.actor

import com.jeanbarrossilva.mastodonte.core.test.TestActorProvider
import com.jeanbarrossilva.mastodonte.core.test.TestAuthenticator
import com.jeanbarrossilva.mastodonte.core.test.TestAuthorizer
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class ActorProviderTests {
    @Test
    fun `GIVEN a provider WHEN authenticating THEN it provides the resulting actor`() {
        val actorProvider = TestActorProvider()
        val authorizer = TestAuthorizer()
        val authenticator = TestAuthenticator(actorProvider)
        runTest {
            val actor = authenticator.authenticate(authorizer)
            assertEquals(actor, actorProvider.provide())
        }
    }
}
