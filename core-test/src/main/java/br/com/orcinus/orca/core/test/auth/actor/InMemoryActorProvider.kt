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

package br.com.orcinus.orca.core.test.auth.actor

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import kotlinx.coroutines.runBlocking

/** [ActorProvider] that remembers and retrieves [Actor]s from memory. */
class InMemoryActorProvider : ActorProvider() {
  /**
   * [Actor] that has been remembered.
   *
   * @see ActorProvider.remember
   */
  private var actor: Actor = Actor.Unauthenticated

  /**
   * Remembers the given [actor] so that it can be retrieved later.
   *
   * @param actor [Actor] to remember.
   * @see retrieve
   */
  fun remember(actor: Actor) {
    /*
     * InMemoryActorProvider's onRemembrance(Actor) doesn't actually suspend, so calling it
     * blockingly doesn't interrupt the execution flow.
     */
    runBlocking { onRemembrance(actor) }
  }

  override suspend fun onRemembrance(actor: Actor) {
    this.actor = actor
  }

  override suspend fun retrieve(): Actor {
    return actor
  }
}
