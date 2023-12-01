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
