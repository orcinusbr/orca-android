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
