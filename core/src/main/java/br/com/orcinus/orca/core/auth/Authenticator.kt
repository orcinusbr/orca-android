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

package br.com.orcinus.orca.core.auth

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider

/** Authenticates a user through [authenticate]. */
abstract class Authenticator @InternalCoreApi constructor() {
  /**
   * [ActorProvider] to which the [authenticated][Actor.Authenticated] [Actor] will be sent to be
   * remembered when authentication occurs.
   */
  protected abstract val actorProvider: ActorProvider

  /**
   * Authenticates the current [Actor].
   *
   * @param authorizationCode Code resulted from the authorization process performed by an
   *   [Authorizer].
   */
  suspend fun authenticate(authorizationCode: AuthorizationCode): Actor {
    val actor = onAuthentication(authorizationCode.value)
    actorProvider.remember(actor)
    return actor
  }

  /**
   * Callback called whenever the current [Actor] is unauthenticated and is requested to be
   * authenticated.
   *
   * @param authorizationCode Unwrapped code resulted from the authorization process performed by an
   *   [Authorizer].
   */
  protected abstract suspend fun onAuthentication(authorizationCode: String): Actor
}
