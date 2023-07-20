package com.jeanbarrossilva.mastodonte.core.auth

/** Authenticates a user through [authenticate]. **/
abstract class Authenticator {
    /** [Actor] resulted from the last authentication. **/
    internal var lastActor: Actor = Actor.Unauthenticated
        private set

    /**
     * Authorizes the user (if they haven't been authorized yet) and then authenticates them.
     *
     * @param authorizer [Authorizer] with which the user may be authorized.
     **/
    suspend fun authenticate(authorizer: Authorizer): Actor {
        return when (val lastActor = lastActor) {
            is Actor.Unauthenticated ->
                authenticateThroughAuthorizer(authorizer).also { this.lastActor = it }
            is Actor.Authenticated ->
                lastActor
        }
    }

    /**
     * Authenticates the user.
     *
     * @param authorizationCode Code that resulted from authorizing the user.
     * @return Access token to be used in operations that require authentication.
     **/
    protected abstract suspend fun onAuthenticate(authorizationCode: String): Actor.Authenticated

    /**
     * Authorizes the user with the authorization code provided by the [authorizer] and then
     * authenticates them.
     *
     * @param authorizer [Authorizer] with which the user will be authorized.
     **/
    private suspend fun authenticateThroughAuthorizer(authorizer: Authorizer): Actor.Authenticated {
        val authorizationCode = authorizer._authorize()
        return onAuthenticate(authorizationCode)
    }
}
