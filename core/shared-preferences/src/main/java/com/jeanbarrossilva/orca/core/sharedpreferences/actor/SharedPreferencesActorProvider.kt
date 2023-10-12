package com.jeanbarrossilva.orca.core.sharedpreferences.actor

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.mirror.MirroredActor
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.mirror.toMirroredActor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SharedPreferencesActorProvider(context: Context) : ActorProvider() {
  private val preferences: SharedPreferences =
    context.getSharedPreferences("shared-preferences-actor-provider", Context.MODE_PRIVATE)

  override suspend fun remember(actor: Actor) {
    val mirroredActor = actor.toMirroredActor()
    val mirroredActorAsJson = Json.encodeToString(mirroredActor)
    preferences.edit { putString(MIRRORED_ACTOR_KEY, mirroredActorAsJson) }
  }

  override suspend fun retrieve(): Actor {
    return preferences
      .getString(MIRRORED_ACTOR_KEY, null)
      ?.let { Json.decodeFromString<MirroredActor>(it) }
      ?.toActor() ?: Actor.Unauthenticated
  }

  internal fun reset() {
    preferences.edit { remove(MIRRORED_ACTOR_KEY) }
  }

  companion object {
    private const val MIRRORED_ACTOR_KEY = "mirrored-actor"
  }
}
