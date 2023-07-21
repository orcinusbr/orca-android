package com.jeanbarrossilva.mastodonte.core.sample.auth

import com.jeanbarrossilva.mastodonte.core.auth.Actor
import com.jeanbarrossilva.mastodonte.core.auth.ActorProvider
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator

/** [Authenticator] that provides a sample [Actor]. **/
class SampleAuthenticator(override val actorProvider: ActorProvider) : Authenticator() {
    override suspend fun onAuthenticate(authorizationCode: String): Actor {
        return Actor.Authenticated("sample-access-token")
    }
}
