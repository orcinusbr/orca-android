package com.jeanbarrossilva.mastodonte.core.auth

/** Authenticates a user through [authenticate]. **/
abstract class Authenticator {
    /**
     * Authorizes the user and then tries to authenticates them.
     *
     * @param authorizer [Authorizer] with which the user will be authorized.
     **/
    suspend fun authenticate(authorizer: Authorizer): Actor {
        val authorizationCode = authorizer._authorize()
        return onAuthenticate(authorizationCode)
    }

    /**
     * Authenticates the user.
     *
     * @param authorizationCode Code that resulted from authorizing the user.
     **/
    protected abstract suspend fun onAuthenticate(authorizationCode: String): Actor
}
