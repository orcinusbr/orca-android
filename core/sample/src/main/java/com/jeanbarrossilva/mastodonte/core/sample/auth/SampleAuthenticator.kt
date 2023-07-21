package com.jeanbarrossilva.mastodonte.core.sample.auth

import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider

/** [Authenticator] that provides a sample [Actor]. **/
class SampleAuthenticator(override val actorProvider: ActorProvider) : Authenticator() {
    override suspend fun onAuthenticate(authorizationCode: String): Actor {
        return Actor.Authenticated("sample-access-token")
    }
}
