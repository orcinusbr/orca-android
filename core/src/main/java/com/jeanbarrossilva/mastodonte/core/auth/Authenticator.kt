package com.jeanbarrossilva.mastodonte.core.auth

/** Authenticates a user through [authenticate]. **/
abstract class Authenticator {
    /**
     * Authorizes the user (if they haven't been authorized yet) and then authenticates them.
     *
     * @param authorizer [Authorizer] with which the user may be authorized.
     **/
    suspend fun authenticate(authorizer: Authorizer): Actor.Authenticated {
        val authorizationCode = authorizer._authorize()
        return onAuthenticate(authorizationCode)
    }

    /**
     * Authenticates the user.
     *
     * @param authorizationCode Code that resulted from authorizing the user.
     **/
    protected abstract suspend fun onAuthenticate(authorizationCode: String): Actor.Authenticated
}
