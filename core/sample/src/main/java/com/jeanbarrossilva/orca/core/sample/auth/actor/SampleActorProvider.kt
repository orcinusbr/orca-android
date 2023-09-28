package com.jeanbarrossilva.orca.core.sample.auth.actor

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider

/** [ActorProvider] that always provides a sample [authenticated][Actor.Authenticated] [Actor]. **/
object SampleActorProvider : ActorProvider() {
    override suspend fun remember(actor: Actor) {
    }

    override suspend fun retrieve(): Actor {
        return Actor.Authenticated.sample
    }
}
