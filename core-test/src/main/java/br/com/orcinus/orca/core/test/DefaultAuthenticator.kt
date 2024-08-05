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
import br.com.orcinus.orca.core.test.auth.AuthorizerBuilder

/**
 * [Authenticator] that calls the [callback] and returns an [Actor] provided by the [actorProvider]
 * when authentication is performed.
 *
 * @param authorizer [Authorizer] with which the user will be authorized.
 * @param actorProvider [ActorProvider] that provides the [Actor] to be returned upon
 *   authentication.
 * @param callback Operation to be performed when [onAuthenticate] is called.
 */
class DefaultAuthenticator(
  override val authorizer: Authorizer = AuthorizerBuilder().build(),
  override val actorProvider: ActorProvider = InMemoryActorProvider(),
  private val callback: suspend (authorizationCode: String) -> Unit = noOpCallback
) : Authenticator() {
  override suspend fun onAuthenticate(authorizationCode: String): Actor {
    callback(authorizationCode)
    return actorProvider.provide()
  }

  companion object {
    /** No-op callback that is the default one of a [DefaultAuthenticator]. */
    private val noOpCallback: suspend (authorizationCode: String) -> Unit = {}
  }
}
