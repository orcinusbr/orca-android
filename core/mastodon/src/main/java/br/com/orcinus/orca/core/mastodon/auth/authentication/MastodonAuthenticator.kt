/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.auth.authentication

import android.content.Context
import br.com.orcinus.orca.core.auth.Authenticator
import br.com.orcinus.orca.core.auth.Authorizer
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.mastodon.auth.authentication.activity.MastodonAuthenticationActivity
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive

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

  /**
   * Listeners to be notified when an [Actor] is received, before [onAuthentication] returns.
   *
   * @see receive
   */
  private val onActorReceiptListeners = mutableListOf<(Actor) -> Unit>()

  override suspend fun onAuthentication(authorizationCode: String): Actor {
    return suspendCoroutine {
      continuation = it
      MastodonAuthenticationActivity.start(context, authorizationCode)
    }
  }

  /**
   * Adds a listener to be notified when an [Actor] is received.
   *
   * @param onActorReceiptListener Listener lambda that will be invoked with the received [Actor].
   */
  internal fun addOnActorReceiptListener(onActorReceiptListener: (Actor) -> Unit) {
    val isActorYetToBeReceived = continuation?.context?.get(Job)?.isActive ?: true
    if (isActorYetToBeReceived) {
      onActorReceiptListeners.add(onActorReceiptListener)
    }
  }

  /**
   * Removes a listener that would be notified when an [Actor] were to be received.
   *
   * @param onActorReceiptListener Listener to be removed.
   */
  internal fun removeOnActorReceiptListener(onActorReceiptListener: (Actor) -> Unit) {
    onActorReceiptListeners.remove(onActorReceiptListener)
  }

  /**
   * Notifies this [MastodonAuthenticator] that the [actor] has been successfully retrieved,
   * consequently notifying the added on-actor-receipt listeners and resuming the suspended
   * coroutine.
   *
   * @param actor [Actor] to be received.
   * @see onActorReceiptListeners
   */
  internal fun receive(actor: Actor) {
    continuation?.takeIf { it.context.isActive }?.resume(actor)
    onActorReceiptListeners.onEach { it(actor) }.clear()
  }
}
