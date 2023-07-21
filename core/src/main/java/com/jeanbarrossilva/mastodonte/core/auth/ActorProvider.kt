package com.jeanbarrossilva.mastodonte.core.auth

/** Provides an [Actor] through [provide]. **/
abstract class ActorProvider {
    /** [Authorizer] that will authorize authentication through the [authorizer]. **/
    protected abstract val authorizer: Authorizer

    /**
     * [Authenticator] that enables falling back to authentication in order to provide the [Actor].
     **/
    protected abstract val authenticator: Authenticator

    /**
     * Authenticates the user and provides the resulting [authenticated][Actor.Authenticated]
     * [Actor] or retrieves the previous one.
     *
     * @see retrieve
     **/
    suspend fun provide(): Actor.Authenticated {
        return when (val retrieved = retrieve()) {
            is Actor.Unauthenticated -> authenticator.authenticate(authorizer).also { remember(it) }
            is Actor.Authenticated -> retrieved
        }
    }

    /**
     * Remembers the given [actor] so that it can be retrieved later.
     *
     * @see retrieve
     **/
    protected abstract suspend fun remember(actor: Actor.Authenticated)

    /** Retrieves an [Actor]. **/
    protected abstract suspend fun retrieve(): Actor
}
