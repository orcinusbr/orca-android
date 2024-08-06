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

package br.com.orcinus.orca.core.auth

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider

/** Authenticates a user through [authenticate]. */
abstract class Authenticator @InternalCoreApi constructor() {
  /** [Authorizer] with which the user will be authorized. */
  protected abstract val authorizer: Authorizer

  /**
   * [ActorProvider] to which the [authenticated][Actor.Authenticated] [Actor] will be sent to be
   * remembered when authentication occurs.
   */
  protected abstract val actorProvider: ActorProvider

  /** Authorizes the user with the [authorizer] and then tries to authenticates them. */
  suspend fun authenticate(): Actor {
    val authorizationCode = authorizer.authorize()
    val actor = onAuthentication(authorizationCode)
    actorProvider.remember(actor)
    return actor
  }

  /**
   * Tries to authenticate the user.
   *
   * @param authorizationCode Code that resulted from authorizing the user.
   */
  protected abstract suspend fun onAuthentication(authorizationCode: String): Actor
}
