package com.jeanbarrossilva.orca.core.auth.actor

/** Provides an [Actor] through [provide]. **/
abstract class ActorProvider {
    /** Provides an [Actor]. **/
    suspend fun provide(): Actor {
        return retrieve()
    }

    /**
     * Remembers the given [actor] so that it can be retrieved later.
     *
     * @see retrieve
     **/
    @Suppress("FunctionName")
    internal suspend fun _remember(actor: Actor) {
        remember(actor)
    }

    /**
     * Remembers the given [actor] so that it can be retrieved later.
     *
     * @see retrieve
     **/
    protected abstract suspend fun remember(actor: Actor)

    /**
     * Retrieves a remembered [Actor].
     *
     * @see remember
     **/
    protected abstract suspend fun retrieve(): Actor
}
