/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
