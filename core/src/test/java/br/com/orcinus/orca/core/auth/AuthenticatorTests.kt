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

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.test.ConstantAuthorizer
import br.com.orcinus.orca.core.test.TestAuthenticator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class AuthenticatorTests {
  @Test
  fun `GIVEN an authentication WHEN verifying if the actor is authorized THEN it is`() {
    var isAuthorized = false
    val authorizer = ConstantAuthorizer { isAuthorized = true }
    runTest { TestAuthenticator(authorizer).authenticate() }
    assertTrue(isAuthorized)
  }

  @Test
  fun `GIVEN an authentication WHEN comparing the authorization code provided by the authorizer and the one the authenticator receives THEN they're the same`() {
    lateinit var providedAuthorizationCode: String
    val authenticator = TestAuthenticator { providedAuthorizationCode = it }
    runTest { authenticator.authenticate() }
    assertEquals(ConstantAuthorizer.AUTHORIZATION_CODE, providedAuthorizationCode)
  }

  @Test
  fun `GIVEN an authentication WHEN getting the resulting actor THEN it's authenticated`() {
    val authenticator = TestAuthenticator()
    runTest { assertIs<Actor.Authenticated>(authenticator.authenticate()) }
  }
}
