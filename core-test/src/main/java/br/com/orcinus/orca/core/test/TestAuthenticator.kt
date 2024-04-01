/*
 * Copyright © 2023-2024 Orca
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
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.std.image.test.TestImageLoader

/**
 * [Authenticator] that switches the [Actor] locally on authentication.
 *
 * @param authorizer [TestAuthorizer] with which the user will be authorized.
 * @param actorProvider [TestActorProvider] to which the [authenticated][Actor.Authenticated]
 *   [Actor] will be sent to be remembered when authentication occurs.
 * @param onOnAuthenticate Operation to be performed when [onAuthenticate] is called.
 * @see currentActor
 * @see switchCurrentActor
 */
class TestAuthenticator(
  override val authorizer: TestAuthorizer = TestAuthorizer(),
  override val actorProvider: TestActorProvider = TestActorProvider(),
  private val onOnAuthenticate: suspend (authorizationCode: String) -> Unit = {}
) : Authenticator() {
  /**
   * [Actor] to be switched on [onAuthenticate].
   *
   * @see switchCurrentActor
   */
  private var currentActor: Actor = Actor.Unauthenticated

  /**
   * [Authenticated][Actor.Authenticated] [Actor] to switch to when the [currentActor] is
   * [unauthenticated][Actor.Unauthenticated].
   */
  private val authenticatedActor =
    Actor.Authenticated("test-id", "test-access-token", TestImageLoader)

  override suspend fun onAuthenticate(authorizationCode: String): Actor {
    onOnAuthenticate(authorizationCode)
    switchCurrentActor()
    return currentActor
  }

  /**
   * Switches the [currentActor] to an [authenticated][Actor.Authenticated] one if it's
   * [unauthenticated][Actor.Unauthenticated] or vice-versa.
   */
  private fun switchCurrentActor() {
    currentActor =
      when (currentActor) {
        is Actor.Unauthenticated -> authenticatedActor
        is Actor.Authenticated -> Actor.Unauthenticated
      }
  }
}
