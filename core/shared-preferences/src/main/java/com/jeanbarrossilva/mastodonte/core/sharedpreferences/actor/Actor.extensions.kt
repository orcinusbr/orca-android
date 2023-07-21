package com.jeanbarrossilva.mastodonte.core.sharedpreferences.actor

import com.jeanbarrossilva.mastodonte.core.auth.Actor

/** Converts this [Actor] into an [MirroredActor]. **/
internal fun Actor.toMirroredActor(): MirroredActor {
    return when (this) {
        is Actor.Unauthenticated -> MirroredActor.unauthenticated()
        is Actor.Authenticated -> MirroredActor.authenticated(accessToken)
    }
}
