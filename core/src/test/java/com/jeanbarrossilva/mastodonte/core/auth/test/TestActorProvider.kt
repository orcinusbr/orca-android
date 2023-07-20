package com.jeanbarrossilva.mastodonte.core.auth.test

import com.jeanbarrossilva.mastodonte.core.auth.Actor
import com.jeanbarrossilva.mastodonte.core.auth.ActorProvider

/**
 * [ActorProvider] that provides an [authenticated][Actor.Authenticated] [Actor] through a
 * [TestAuthenticator].
 *
 * @param authenticator [TestAuthenticator] through which the [authenticated][Actor.Authenticated]
 * [Actor] will be provided.
 **/
internal class TestActorProvider(
    override val authenticator: TestAuthenticator = TestAuthenticator()
) : ActorProvider()
