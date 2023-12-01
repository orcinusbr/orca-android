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
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider

/** Authenticates a user through [authenticate]. */
abstract class Authenticator {
  /** [Authorizer] with which the user will be authorized. */
  protected abstract val authorizer: Authorizer

  /**
   * [ActorProvider] to which the [authenticated][Actor.Authenticated] [Actor] will be sent to be
   * remembered when authentication occurs.
   */
  protected abstract val actorProvider: ActorProvider

  /** Authorizes the user with the [authorizer] and then tries to authenticates them. */
  suspend fun authenticate(): Actor {
    val authorizationCode = authorizer._authorize()
    val actor = onAuthenticate(authorizationCode)
    actorProvider._remember(actor)
    return actor
  }

  /**
   * Tries to authenticate the user.
   *
   * @param authorizationCode Code that resulted from authorizing the user.
   */
  protected abstract suspend fun onAuthenticate(authorizationCode: String): Actor
}
