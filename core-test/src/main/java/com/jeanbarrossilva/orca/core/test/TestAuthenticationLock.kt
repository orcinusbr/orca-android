/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.test

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor

/**
 * [AuthenticationLock] with test-specific default structures.
 *
 * @param authenticator [TestAuthenticator] through which the [Actor] will be authenticated if it
 *   isn't and [requestUnlock][AuthenticationLock.requestUnlock] is called.
 * @param actorProvider [TestActorProvider] whose provided [Actor] will be ensured to be either
 *   [unauthenticated][Actor.Unauthenticated] or [authenticated][Actor.Authenticated].
 */
@Suppress("FunctionName")
fun TestAuthenticationLock(
  actorProvider: TestActorProvider = TestActorProvider(),
  authenticator: TestAuthenticator = TestAuthenticator(actorProvider = actorProvider)
): AuthenticationLock<TestAuthenticator> {
  return AuthenticationLock(authenticator, actorProvider)
}
