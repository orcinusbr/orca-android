package com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication

import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import kotlinx.serialization.Serializable

@Serializable
internal data class Token(val accessToken: String) {
    fun toActor(): Actor.Authenticated {
        return Actor.Authenticated(accessToken)
    }
}
