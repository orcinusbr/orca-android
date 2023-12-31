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

  companion object
}
