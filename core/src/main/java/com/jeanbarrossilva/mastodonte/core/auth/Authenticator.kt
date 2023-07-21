package com.jeanbarrossilva.mastodonte.core.auth

/** Authenticates a user through [authenticate]. **/
abstract class Authenticator {
    /**
     * [ActorProvider] to which the [authenticated][Actor.Authenticated] [Actor] will be sent to be
     * remembered when authentication occurs.
     **/
    protected abstract val actorProvider: ActorProvider

    /**
     * Authorizes the user and then tries to authenticates them.
     *
     * @param authorizer [Authorizer] with which the user will be authorized.
     **/
    suspend fun authenticate(authorizer: Authorizer): Actor {
        val authorizationCode = authorizer._authorize()
        val actor = onAuthenticate(authorizationCode)
        actorProvider._remember(actor)
        return actor
    }

    /**
     * Authenticates the user.
     *
     * @param authorizationCode Code that resulted from authorizing the user.
     **/
    protected abstract suspend fun onAuthenticate(authorizationCode: String): Actor
}
