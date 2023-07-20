package com.jeanbarrossilva.mastodonte.core.sample.auth

import com.jeanbarrossilva.mastodonte.core.auth.Actor
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator

/** [Authenticator] that provides a sample [authenticated][Actor.Authenticated] [Actor]. **/
object SampleAuthenticator : Authenticator() {
    override suspend fun onAuthenticate(authorizationCode: String): Actor.Authenticated {
        return Actor.Authenticated("sample-access-token")
    }
}
