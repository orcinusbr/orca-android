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
