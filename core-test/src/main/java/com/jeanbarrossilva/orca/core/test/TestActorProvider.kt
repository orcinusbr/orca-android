/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.test

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider

/** [ActorProvider] that remembers and retrieves [Actor]s locally. */
open class TestActorProvider : ActorProvider() {
  /**
   * [Actor] that's been remembered.
   *
   * @see remember
   */
  private var rememberedActor: Actor = Actor.Unauthenticated

  override suspend fun remember(actor: Actor) {
    rememberedActor = actor
  }

  override suspend fun retrieve(): Actor {
    return rememberedActor
  }
}
