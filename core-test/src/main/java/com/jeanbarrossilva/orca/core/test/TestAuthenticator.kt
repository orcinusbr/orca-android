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

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.actor.Actor

/**
 * [Authenticator] that switches the [Actor] locally on authentication.
 *
 * @param authorizer [TestAuthorizer] with which the user will be authorized.
 * @param actorProvider [TestActorProvider] to which the [authenticated][Actor.Authenticated]
 *   [Actor] will be sent to be remembered when authentication occurs.
 * @param onOnAuthenticate Operation to be performed when [onAuthenticate] is called.
 * @see currentActor
 * @see switchCurrentActor
 */
class TestAuthenticator(
  override val authorizer: TestAuthorizer = TestAuthorizer(),
  override val actorProvider: TestActorProvider = TestActorProvider(),
  private val onOnAuthenticate: suspend (authorizationCode: String) -> Unit = {}
) : Authenticator() {
  /**
   * [Actor] to be switched on [onAuthenticate].
   *
   * @see switchCurrentActor
   */
  private var currentActor: Actor = Actor.Unauthenticated

  /**
   * [Authenticated][Actor.Authenticated] [Actor] to switch to when the [currentActor] is
   * [unauthenticated][Actor.Unauthenticated].
   */
  private val authenticatedActor = Actor.Authenticated("test-id", "test-access-token")

  override suspend fun onAuthenticate(authorizationCode: String): Actor {
    onOnAuthenticate(authorizationCode)
    switchCurrentActor()
    return currentActor
  }

  /**
   * Switches the [currentActor] to an [authenticated][Actor.Authenticated] one if it's
   * [unauthenticated][Actor.Unauthenticated] or vice-versa.
   */
  private fun switchCurrentActor() {
    currentActor =
      when (currentActor) {
        is Actor.Unauthenticated -> authenticatedActor
        is Actor.Authenticated -> Actor.Unauthenticated
      }
  }
}
