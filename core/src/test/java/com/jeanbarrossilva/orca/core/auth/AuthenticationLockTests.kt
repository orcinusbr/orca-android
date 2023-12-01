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

package com.jeanbarrossilva.orca.core.auth

import com.jeanbarrossilva.orca.core.test.TestActorProvider
import com.jeanbarrossilva.orca.core.test.TestAuthenticationLock
import com.jeanbarrossilva.orca.core.test.TestAuthenticator
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class AuthenticationLockTests {
  @Test
  fun `GIVEN an unauthenticated actor WHEN unlocking THEN it's authenticated`() {
    var hasBeenAuthenticated = false
    val authenticator = TestAuthenticator { hasBeenAuthenticated = true }
    runTest { TestAuthenticationLock(authenticator = authenticator).requestUnlock {} }
    assertTrue(hasBeenAuthenticated)
  }

  @Test
  fun `GIVEN an authenticated actor WHEN unlocking THEN the listener is notified`() {
    val actorProvider = TestActorProvider()
    val authenticator = TestAuthenticator(actorProvider = actorProvider)
    var hasListenerBeenNotified = false
    runTest {
      authenticator.authenticate()
      TestAuthenticationLock(actorProvider, authenticator).requestUnlock {
        hasListenerBeenNotified = true
      }
    }
    assertTrue(hasListenerBeenNotified)
  }
}
