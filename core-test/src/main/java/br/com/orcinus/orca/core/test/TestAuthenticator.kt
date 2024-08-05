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

package br.com.orcinus.orca.core.test

import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.auth.Authorizer
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider

/**
 * [Authenticator] that switches the [Actor] locally on authentication.
 *
 * @param authorizer [Authorizer] with which the user will be authorized.
 * @param actorProvider [ActorProvider] to which the authenticated [Actor] will be sent to be
 *   remembered when authentication occurs.
 * @param onOnAuthenticate Operation to be performed when [onAuthenticate] is called.
 */
class TestAuthenticator(
  override val authorizer: TestAuthorizer = TestAuthorizer(),
  override val actorProvider: ActorProvider = InMemoryActorProvider(),
  private val onOnAuthenticate: suspend (authorizationCode: String) -> Unit = {}
) : Authenticator() {
  /** Current actor. */
  private var actor: Actor = Actor.Unauthenticated

  override suspend fun onAuthenticate(authorizationCode: String): Actor {
    onOnAuthenticate(authorizationCode)
    actor =
      when (actor) {
        is Actor.Unauthenticated -> actorProvider.provide()
        is Actor.Authenticated -> actor
      }
    return actor
  }
}
