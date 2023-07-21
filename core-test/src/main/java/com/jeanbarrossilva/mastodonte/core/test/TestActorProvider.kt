package com.jeanbarrossilva.mastodonte.core.test

import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider

/** [ActorProvider] that remembers and retrieves [Actor]s locally. **/
class TestActorProvider : ActorProvider() {
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
