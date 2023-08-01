package com.jeanbarrossilva.orca.core.sample.auth

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider

/**
 * [Authenticator] that provides a sample [Actor].
 *
 * @param actorProvider [ActorProvider] to which the [authenticated][Actor.Authenticated] [Actor]
 * will be sent to be remembered when authentication occurs.
 **/
class SampleAuthenticator(
    override val actorProvider: ActorProvider
) : Authenticator() {
    override val authorizer: Authorizer = SampleAuthorizer

    override suspend fun onAuthenticate(authorizationCode: String): Actor {
        return Actor.Authenticated("sample-access-token")
    }
}
