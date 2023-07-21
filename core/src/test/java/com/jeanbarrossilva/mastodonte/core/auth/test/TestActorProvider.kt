package com.jeanbarrossilva.mastodonte.core.auth.test

import com.jeanbarrossilva.mastodonte.core.auth.Actor
import com.jeanbarrossilva.mastodonte.core.auth.ActorProvider
import com.jeanbarrossilva.mastodonte.core.test.TestAuthenticator
import com.jeanbarrossilva.mastodonte.core.test.TestAuthorizer

/**
 * [ActorProvider] that provides an [authenticated][Actor.Authenticated] [Actor] through a
 * [TestAuthorizer] and a [TestAuthenticator].
 *
 * @param authorizer [TestAuthorizer] through which authentication will be performed.
 * @param authenticator [TestAuthenticator] through which the [authenticated][Actor.Authenticated]
 * [Actor] will be provided.
 **/
internal class TestActorProvider(
    override val authorizer: TestAuthorizer = TestAuthorizer(),
    override val authenticator: TestAuthenticator = TestAuthenticator()
) : ActorProvider() {
    /**
     * [Actor] that's been remembered.
     *
     * @see remember
     **/
    var rememberedActor: Actor = Actor.Unauthenticated
        private set

    override suspend fun remember(actor: Actor.Authenticated) {
        rememberedActor = actor
    }

    override suspend fun retrieve(): Actor {
        return rememberedActor
    }
}
