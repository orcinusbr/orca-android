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

package br.com.orcinus.orca.core.auth.actor

/** Provides an [Actor] through [provide]. */
abstract class ActorProvider {
  /** Provides an [Actor]. */
  suspend fun provide(): Actor {
    return retrieve()
  }

  /**
   * Remembers the given [actor] so that it can be retrieved later.
   *
   * @see retrieve
   */
  @Suppress("FunctionName")
  internal suspend fun _remember(actor: Actor) {
    remember(actor)
  }

  /**
   * Remembers the given [actor] so that it can be retrieved later.
   *
   * @see retrieve
   */
  protected abstract suspend fun remember(actor: Actor)

  /**
   * Retrieves a remembered [Actor].
   *
   * @see remember
   */
  protected abstract suspend fun retrieve(): Actor

  companion object
}
