package com.jeanbarrossilva.mastodonte.core.sharedpreferences.actor.mirror

import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import kotlinx.serialization.Serializable

@Serializable
internal class MirroredActor private constructor(
    private val type: String,
    val accessToken: String?
) {
    init {
        ensureIntegrity()
    }

    fun toActor(): Actor {
        return either({ Actor.Unauthenticated }) {
            Actor.Authenticated(accessToken!!)
        }
    }

    private fun ensureIntegrity() {
        either(::ensureUnauthenticatedIntegrity, ::ensureAuthenticatedIntegrity)
    }

    private fun <T> either(unauthenticated: () -> T, authenticated: () -> T): T {
        return when (type) {
            UNAUTHENTICATED_TYPE -> unauthenticated()
            AUTHENTICATED_TYPE -> authenticated()
            else -> throw IllegalArgumentException("Unknown \"$type\" mirrored actor type.")
        }
    }

    private fun ensureUnauthenticatedIntegrity() {
        require(accessToken == null) {
            "Unauthenticated mirrored actor shouldn't have an access token (\"$accessToken\")."
        }
    }

    private fun ensureAuthenticatedIntegrity() {
        require(accessToken != null) {
            "Authenticated mirrored actor should have an access token."
        }
    }

    companion object {
        private const val UNAUTHENTICATED_TYPE = "unauthenticated"
        private const val AUTHENTICATED_TYPE = "authenticated"

        fun unauthenticated(): MirroredActor {
            return MirroredActor(UNAUTHENTICATED_TYPE, accessToken = null)
        }

        fun authenticated(accessToken: String): MirroredActor {
            return MirroredActor(AUTHENTICATED_TYPE, accessToken)
        }
    }
}
