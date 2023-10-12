package com.jeanbarrossilva.orca.core.auth

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider

/** An [AuthenticationLock] with a generic [Authenticator]. */
typealias SomeAuthenticationLock = AuthenticationLock<*>

/**
 * Ensures that an operation is only performed by an [authenticated][Actor.Authenticated] [Actor],
 * through [requestUnlock].
 *
 * @param T [Authenticator] to authenticate the [Actor] with.
 * @param authenticator [Authenticator] through which the [Actor] will be requested to be
 *   [authenticated][Actor.Authenticated].
 * @param actorProvider [ActorProvider] whose provided [Actor] will be ensured to be
 *   [authenticated][Actor.Authenticated].
 */
class AuthenticationLock<T : Authenticator>(
  private val authenticator: T,
  private val actorProvider: ActorProvider
) {
  /** [IllegalStateException] thrown if authentication fails. */
  class FailedAuthenticationException internal constructor() :
    IllegalStateException("Could not authenticate properly.")

  /**
   * Listens to an unlock.
   *
   * @param T Value returned by [onUnlock].
   */
  fun interface OnUnlockListener<T> {
    /**
     * Callback called when the [Actor] provided by the [actorProvider] is
     * [authenticated][Actor.Authenticated].
     *
     * @param actor Provided [authenticated][Actor.Authenticated] [Actor].
     */
    suspend fun onUnlock(actor: Actor.Authenticated): T
  }

  /**
   * Ensures that the operation in the [listener]'s [onUnlock][OnUnlockListener.onUnlock] callback
   * is only performed if the [Actor] is [authenticated][Actor.Authenticated]; if it isn't, then
   * authentication is requested and, if it succeeds, the operation is performed.
   *
   * @param T Value returned by the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
   * @param listener [OnUnlockListener] to be notified if the [Actor] is
   *   [authenticated][Actor.Authenticated].
   * @return Result of the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
   * @throws FailedAuthenticationException If authentication fails.
   */
  suspend fun <T> requestUnlock(listener: OnUnlockListener<T>): T {
    return when (val actor = actorProvider.provide()) {
      is Actor.Unauthenticated -> authenticateAndNotify(listener)
      is Actor.Authenticated -> listener.onUnlock(actor)
    }
  }

  /**
   * Authenticates and notifies the [listener] if the resulting [Actor] is
   * [authenticated][Actor.Authenticated].
   *
   * @param T Value returned by the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
   * @param listener [OnUnlockListener] to be notified if the [Actor] is
   *   [authenticated][Actor.Authenticated].
   * @throws FailedAuthenticationException If authentication fails.
   */
  private suspend fun <T> authenticateAndNotify(listener: OnUnlockListener<T>): T {
    val actor = authenticator.authenticate()
    return if (actor is Actor.Authenticated) {
      listener.onUnlock(actor)
    } else {
      throw FailedAuthenticationException()
    }
  }
}
