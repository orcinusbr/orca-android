/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.core.sample.auth.actor

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider

/**
 * [ActorProvider] that always provides the same [Actor], which is a sample one by default.
 *
 * @property actor [Actor] to be ever-provided.
 */
class SampleActorProvider(private var actor: Actor) : ActorProvider() {
  @Deprecated(
    "This constructor implicitly defines the actor as one whose avatar is loaded by a no-op " +
      "loader. Prefer specifying the actor to be provided instead — which can be instantiated " +
      "through Actor.Authenticated.Companion.createSample(ImageLoader<*, *>) — by calling the " +
      "constructor that receives one."
  )
  constructor() : this(Actor.Authenticated.sample)

  override suspend fun onRemembrance(actor: Actor) {
    this.actor = actor
  }

  override suspend fun retrieve() = actor
}
