package com.jeanbarrossilva.mastodonte.core.sharedpreferences.actor

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider
import com.jeanbarrossilva.mastodonte.core.sharedpreferences.actor.mirror.MirroredActor
import com.jeanbarrossilva.mastodonte.core.sharedpreferences.actor.mirror.toMirroredActor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SharedPreferencesActorProvider(context: Context) : ActorProvider() {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("shared-preferences-actor-provider", Context.MODE_PRIVATE)

    override suspend fun remember(actor: Actor) {
        val unstableActor = actor.toMirroredActor()
        val unstableActorAsJson = Json.encodeToString(unstableActor)
        preferences.edit { putString(UNSTABLE_ACTOR_KEY, unstableActorAsJson) }
    }

    override suspend fun retrieve(): Actor {
        return preferences.getString(UNSTABLE_ACTOR_KEY, null)
            ?.let { Json.decodeFromString<MirroredActor>(it) }
            ?.toActor()
            ?: Actor.Unauthenticated
    }

    internal fun reset() {
        preferences.edit {
            remove(UNSTABLE_ACTOR_KEY)
        }
    }

    companion object {
        private const val UNSTABLE_ACTOR_KEY = "unstable-actor"
    }
}
