package com.jeanbarrosilva.orca.core.http.test

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider

/**
 * [ActorProvider] that provides only the specified [actor].
 *
 * @param actor [Actor] to be provided unconditionally.
 **/
internal class FixedActorProvider(private val actor: Actor) : ActorProvider() {
    override suspend fun remember(actor: Actor) {
    }

    override suspend fun retrieve(): Actor {
        return actor
    }
}
