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

package com.jeanbarrossilva.orca.core.auth

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.test.TestAuthenticator
import com.jeanbarrossilva.orca.core.test.TestAuthorizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class AuthenticatorTests {
  @Test
  fun `GIVEN an authentication WHEN verifying if the actor is authorized THEN it is`() {
    var isAuthorized = false
    val authorizer = TestAuthorizer { isAuthorized = true }
    runTest { TestAuthenticator(authorizer).authenticate() }
    assertTrue(isAuthorized)
  }

  @Test
  fun `GIVEN an authentication WHEN comparing the authorization code provided by the authorizer and the one the authenticator receives THEN they're the same`() {
    lateinit var providedAuthorizationCode: String
    val authorizer = TestAuthorizer()
    val authenticator = TestAuthenticator { providedAuthorizationCode = it }
    runTest { authenticator.authenticate() }
    assertEquals(TestAuthorizer.AUTHORIZATION_CODE, providedAuthorizationCode)
  }

  @Test
  fun `GIVEN an authentication WHEN getting the resulting actor THEN it's authenticated`() {
    val authorizer = TestAuthorizer()
    val authenticator = TestAuthenticator()
    runTest { assertIs<Actor.Authenticated>(authenticator.authenticate()) }
  }
}
