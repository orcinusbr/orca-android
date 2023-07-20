package com.jeanbarrossilva.mastodonte.core.auth

/** Provides an [Actor] through [provide]. **/
abstract class ActorProvider {
    /** [Authenticator] that provides the [Actor] that resulted from the authentication. **/
    protected abstract val authenticator: Authenticator

    /**
     * [IllegalStateException] thrown if an operation that requires authentication is requested to
     * be performed when the [Actor] is [unauthenticated][Actor.Unauthenticated].
     **/
    class UnauthenticatedException internal constructor() : IllegalStateException(
        "This operation cannot be performed because the user is not authenticated."
    )

    /**
     * Provides an [Actor].
     *
     * @throws UnauthenticatedException If the [Actor] is [unauthenticated][Actor.Unauthenticated].
     **/
    fun provide(): Actor.Authenticated {
        return when (val actor = authenticator.lastActor) {
            is Actor.Unauthenticated -> throw UnauthenticatedException()
            is Actor.Authenticated -> actor
        }
    }
}
