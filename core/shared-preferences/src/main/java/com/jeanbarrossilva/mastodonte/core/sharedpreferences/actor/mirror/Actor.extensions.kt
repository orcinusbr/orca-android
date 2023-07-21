package com.jeanbarrossilva.mastodonte.core.sharedpreferences.actor.mirror

import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor

/** Converts this [Actor] into an [MirroredActor]. **/
internal fun Actor.toMirroredActor(): MirroredActor {
    return when (this) {
        is Actor.Unauthenticated -> MirroredActor.unauthenticated()
        is Actor.Authenticated -> MirroredActor.authenticated(accessToken)
    }
}
