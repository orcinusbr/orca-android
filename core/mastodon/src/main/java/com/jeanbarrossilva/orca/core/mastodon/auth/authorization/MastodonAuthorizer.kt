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
