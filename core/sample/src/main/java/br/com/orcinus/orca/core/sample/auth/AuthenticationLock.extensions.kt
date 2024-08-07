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

package br.com.orcinus.orca.core.sample.auth

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider

/** [AuthenticationLock] returned by [sample]. */
private object SampleAuthenticationLock : AuthenticationLock<Authenticator>() {
  override val authenticator = SampleAuthenticator
  override val actorProvider = SampleActorProvider

  override fun createFailedAuthenticationException(): FailedAuthenticationException {
    return FailedAuthenticationException(cause = null)
  }
}

/** Sample [AuthenticationLock]. */
internal val AuthenticationLock.Companion.sample: AuthenticationLock<Authenticator>
  get() = SampleAuthenticationLock
