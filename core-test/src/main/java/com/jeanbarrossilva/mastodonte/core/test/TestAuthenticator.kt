package com.jeanbarrossilva.mastodonte.core.test

import com.jeanbarrossilva.mastodonte.core.auth.Actor
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator

/**
 * [Authenticator] that provides a fixed [authenticated][Actor.Authenticated] [Actor].
 *
 * @param onOnAuthenticate Operation to be performed when [onAuthenticate] is called.
 **/
class TestAuthenticator(
    private val onOnAuthenticate: suspend (authorizationCode: String) -> Unit = { }
) : Authenticator() {
    override suspend fun onAuthenticate(authorizationCode: String): Actor.Authenticated {
        onOnAuthenticate(authorizationCode)
        return Actor.Authenticated("access-token")
    }
}
