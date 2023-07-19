package com.jeanbarrossilva.mastodonte.core.auth

/** Authenticates a user through [authenticate]. **/
abstract class Authenticator {
    /**
     * Authorizes the user with the [authorizer] and then authenticates them.
     *
     * @param authorizer [Authorizer] with which the user will be authorized.
     * @return Access token to be used in operations that require authentication.
     **/
    suspend fun authenticate(authorizer: Authorizer): String {
        val authorizationCode = authorizer.authorize()
        return onAuthenticate(authorizationCode)
    }

    /**
     * Authenticates the user.
     *
     * @param authorizationCode Code that resulted from authorizing the user.
     * @return Access token to be used in operations that require authentication.
     **/
    protected abstract suspend fun onAuthenticate(authorizationCode: String): String
}
