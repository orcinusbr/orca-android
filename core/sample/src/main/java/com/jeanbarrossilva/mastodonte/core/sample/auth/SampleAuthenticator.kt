package com.jeanbarrossilva.mastodonte.core.sample.auth

import com.jeanbarrossilva.mastodonte.core.auth.Authenticator

/** [Authenticator] that provides a sample access token. **/
object SampleAuthenticator : Authenticator() {
    override suspend fun onAuthenticate(authorizationCode: String): String {
        return "sample-access-token"
    }
}
