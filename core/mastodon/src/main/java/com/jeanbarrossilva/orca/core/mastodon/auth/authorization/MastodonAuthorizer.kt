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

package com.jeanbarrossilva.orca.core.mastodon.auth.authorization

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.platform.ui.core.on
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * [Authorizer] that starts a [MastodonAuthorizationActivity] when the user is requested to be
 * authorized and suspends until an access token is received.
 *
 * @param context [Context] through which the [MastodonAuthorizationActivity] will be started.
 * @see receive
 */
class MastodonAuthorizer(private val context: Context) : Authorizer() {
  /** [Continuation] of the coroutine that's suspended on authorization. */
  private var continuation: Continuation<String>? = null

  override suspend fun authorize(): String {
    return suspendCoroutine {
      continuation = it
      context.on<MastodonAuthorizationActivity>().asNewTask().start()
    }
  }

  /**
   * Notifies this [MastodonAuthorizer] that the [accessToken] has been successfully retrieved,
   * consequently resuming the suspended coroutine.
   *
   * @param accessToken Access token to be received.
   */
  internal fun receive(accessToken: String) {
    continuation?.resume(accessToken)
  }
}
