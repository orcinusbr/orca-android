package com.jeanbarrossilva.orca.core.auth

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider

/** Authenticates a user through [authenticate]. **/
abstract class Authenticator {
    /** [Authorizer] with which the user will be authorized. **/
    protected abstract val authorizer: Authorizer

    /**
     * [ActorProvider] to which the [authenticated][Actor.Authenticated] [Actor] will be sent to be
     * remembered when authentication occurs.
     **/
    protected abstract val actorProvider: ActorProvider

    /** Authorizes the user with the [authorizer] and then tries to authenticates them. **/
    suspend fun authenticate(): Actor {
        val authorizationCode = authorizer._authorize()
        val actor = onAuthenticate(authorizationCode)
        actorProvider._remember(actor)
        return actor
    }

    /**
     * Tries to authenticate the user.
     *
     * @param authorizationCode Code that resulted from authorizing the user.
     **/
    protected abstract suspend fun onAuthenticate(authorizationCode: String): Actor
}
