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

package com.jeanbarrossilva.orca.core.mastodon.auth.authentication

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.activity.MastodonAuthenticationActivity
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * [Authenticator] that starts a [MastodonAuthenticationActivity] when the user is requested to be
 * authenticated and suspends until [Actor] is received.
 *
 * @param context [Context] in which the [MastodonAuthenticationActivity] will be started.
 * @see receive
 */
class MastodonAuthenticator(
  private val context: Context,
  override val authorizer: Authorizer,
  override val actorProvider: ActorProvider
) : Authenticator() {
  /** [Continuation] of the coroutine that's suspended on authentication. */
  private var continuation: Continuation<Actor>? = null

  override suspend fun onAuthenticate(authorizationCode: String): Actor {
    return suspendCoroutine {
      continuation = it
      MastodonAuthenticationActivity.start(context, authorizationCode)
    }
  }

  /**
   * Notifies this [MastodonAuthenticator] that the [actor] has been successfully retrieved,
   * consequently resuming the suspended coroutine.
   *
   * @param actor [Actor] to be received.
   */
  internal fun receive(actor: Actor) {
    continuation?.resume(actor)
  }
}
