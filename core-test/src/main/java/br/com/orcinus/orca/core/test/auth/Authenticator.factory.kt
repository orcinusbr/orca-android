/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.core.test.auth

import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider

/**
 * Creates an [Authenticator].
 *
 * @param actorProvider [ActorProvider] to which the authenticated [Actor] will be sent to be
 *   remembered when authentication occurs.
 * @param onAuthenticate Tries to authenticate the user.
 */
fun Authenticator(
  actorProvider: ActorProvider,
  onAuthenticate: suspend (authorizationCode: String) -> Actor = { actorProvider.provide() }
) =
  object : Authenticator() {
    override val actorProvider = actorProvider

    override suspend fun onAuthentication(authorizationCode: String): Actor {
      return onAuthenticate(authorizationCode)
    }
  }
