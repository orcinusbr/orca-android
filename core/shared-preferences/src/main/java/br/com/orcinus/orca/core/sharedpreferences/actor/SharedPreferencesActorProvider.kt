/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.sharedpreferences.actor

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.sharedpreferences.actor.mirror.MirroredActor
import br.com.orcinus.orca.core.sharedpreferences.actor.mirror.image.ImageLoaderProviderFactory
import br.com.orcinus.orca.core.sharedpreferences.actor.mirror.toMirroredActor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SharedPreferencesActorProvider(
  context: Context,
  private val avatarLoaderProviderFactory: ImageLoaderProviderFactory
) : ActorProvider() {
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
      ?.toActor(avatarLoaderProviderFactory)
      ?: Actor.Unauthenticated
  }

  internal fun reset() {
    preferences.edit { remove(MIRRORED_ACTOR_KEY) }
  }

  companion object {
    private const val MIRRORED_ACTOR_KEY = "mirrored-actor"
  }
}
