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
