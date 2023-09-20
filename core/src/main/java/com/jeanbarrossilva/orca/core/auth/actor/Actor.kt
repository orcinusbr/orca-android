package com.jeanbarrossilva.orca.core.auth.actor

/** Agent that can perform operations throughout the application. **/
sealed interface Actor {
    /** Unknown [Actor] that has restricted access. **/
    object Unauthenticated : Actor

    /**
     * [Actor] that's been properly authenticated and can use the application normally.
     *
     * @param id Unique identifier within Mastodon.
     * @param accessToken Access token resulted from the authentication.
     **/
    data class Authenticated(val id: String, val accessToken: String) : Actor {
        companion object
    }
}
