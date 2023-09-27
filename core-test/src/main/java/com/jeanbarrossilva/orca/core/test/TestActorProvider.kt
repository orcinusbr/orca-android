package com.jeanbarrossilva.orca.core.test

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider

/** [ActorProvider] that remembers and retrieves [Actor]s locally. **/
open class TestActorProvider : ActorProvider() {
    /**
     * [Actor] that's been remembered.
     *
     * @see remember
     **/
    private var rememberedActor: Actor = Actor.Unauthenticated

    override suspend fun remember(actor: Actor) {
        rememberedActor = actor
    }

    override suspend fun retrieve(): Actor {
        return rememberedActor
    }
}
