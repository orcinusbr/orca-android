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

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor

/**
 * [AuthenticationLock] with test-specific default structures.
 *
 * @param authenticator [DefaultAuthenticator] through which the [Actor] will be authenticated if it
 *   isn't and [scheduleUnlock] is called.
 * @param actorProvider [InMemoryActorProvider] whose provided [Actor] will be ensured to be either
 *   unauthenticated or authenticated.
 */
class TestAuthenticationLock(
  override val actorProvider: InMemoryActorProvider = InMemoryActorProvider(),
  override val authenticator: DefaultAuthenticator =
    DefaultAuthenticator(actorProvider = actorProvider)
) : AuthenticationLock<DefaultAuthenticator>() {
  override fun createFailedAuthenticationException(): FailedAuthenticationException {
    return FailedAuthenticationException(cause = null)
  }
}
