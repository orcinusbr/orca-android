package com.jeanbarrossilva.mastodonte.core.sample.auth

import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.auth.Authorizer
import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider

/**
 * [Authenticator] that provides a sample [Actor].
 *
 * @param actorProvider [ActorProvider] to which the [authenticated][Actor.Authenticated] [Actor]
 * will be sent to be remembered when authentication occurs.
 **/
class SampleAuthenticator(
    override val actorProvider: ActorProvider,
    override val authorizer: Authorizer = SampleAuthorizer
) : Authenticator() {
    override suspend fun onAuthenticate(authorizationCode: String): Actor {
        return Actor.Authenticated("sample-access-token")
    }
}
