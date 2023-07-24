package com.jeanbarrossilva.mastodonte.core.auth

import com.jeanbarrossilva.mastodonte.core.account.Account
import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider

/** Authenticates a user through [authenticate]. **/
abstract class Authenticator {
    /** [Authorizer] with which the user will be authorized. **/
    protected abstract val authorizer: Authorizer

    /**
     * [ActorProvider] to which the [authenticated][Actor.Authenticated] [Actor] will be sent to be
     * remembered when authentication occurs.
     **/
    protected abstract val actorProvider: ActorProvider

    /**
     * Authorizes with the [authorizer] and then tries to authenticates the [account].
     *
     * @param account [Account] to authenticate.
     **/
    suspend fun authenticate(account: Account): Actor {
        val authorizationCode = authorizer._authorize()
        val actor = onAuthenticate(account, authorizationCode)
        actorProvider._remember(actor)
        return actor
    }

    /**
     * Tries to authenticate the [account].
     *
     * @param account [Account] to authenticate.
     * @param authorizationCode Code that resulted from authorizing the user.
     **/
    protected abstract suspend fun onAuthenticate(account: Account, authorizationCode: String):
        Actor
}
