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

package br.com.orcinus.orca.core.auth

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take

/** An [AuthenticationLock.OnUnlockListener] with a generic return type. */
private typealias SomeOnUnlockListener = AuthenticationLock.OnUnlockListener<*>

/** An [AuthenticationLock] with a generic [Authenticator]. */
typealias SomeAuthenticationLock = AuthenticationLock<*>

/**
 * Ensures that operations are only performed by an authenticated [Actor].
 *
 * @param T [Authenticator] to authenticate the [Actor] with.
 * @see scheduleUnlock
 */
abstract class AuthenticationLock<T : Authenticator> @InternalCoreApi constructor() {
  /**
   * Authenticated [Actor] which has been obtained from the [actorProvider] before an initial
   * unlock.
   */
  private val actorFlow = MutableStateFlow<Actor.Authenticated?>(null)

  /**
   * [Continuation]s associated to their respective [OnUnlockListener]s of unlocks that are awaiting
   * the one being currently performed.
   */
  private val schedule = hashMapOf<SomeOnUnlockListener, Continuation<*>>()

  /** [Authenticator] through which the [Actor] will be requested to be authenticated. */
  protected abstract val authenticator: T

  /** [ActorProvider] whose provided [Actor] will be ensured to be authenticated. */
  protected abstract val actorProvider: ActorProvider

  /**
   * Result of a successful unlock.
   *
   * @param R Type of the [value].
   * @param actor [Actor] that has been authenticated.
   * @param value [R] that's been returned by the [OnUnlockListener].
   */
  private class Unlock<R>(val actor: Actor.Authenticated, val value: R)

  /** [IllegalStateException] thrown if authentication fails. */
  class FailedAuthenticationException @InternalCoreApi constructor(override val cause: Throwable?) :
    IllegalStateException("Authentication has failed.")

  /**
   * Listens to an unlock.
   *
   * @param R Value returned by [onUnlock].
   */
  fun interface OnUnlockListener<R> {
    /**
     * Callback run when the [Actor] provided by the [actorProvider] is authenticated.
     *
     * @param actor Provided authenticated [Actor].
     */
    suspend fun onUnlock(actor: Actor.Authenticated): R
  }

  /**
   * Ensures that the operation in the [listener]'s callback is only performed when the [Actor] is
   * authenticated; if it isn't, then authentication is requested and, if it succeeds, the operation
   * is performed.
   *
   * If an unlock has already been requested and is still ongoing, this one is queued for it to be
   * run as soon as the current finishes (suspending this method's execution flow until then),
   * reusing the [Actor] that's been first obtained from the [actorProvider] by the preceding
   * unlock.
   *
   * @param T Value returned by the [listener]'s callback.
   * @param listener [OnUnlockListener] to be notified when the [Actor] is authenticated.
   * @throws FailedAuthenticationException If authentication fails.
   */
  @Throws(FailedAuthenticationException::class)
  suspend fun <T> scheduleUnlock(listener: OnUnlockListener<T>): T {
    return actorFlow.first()?.let { _ -> awaitUnlock(listener) } ?: requestUnlock(listener)
  }

  /** Creates a variant-specific [FailedAuthenticationException]. */
  protected abstract fun createFailedAuthenticationException(): FailedAuthenticationException

  /**
   * Suspends until the [Continuation] associated to the given [listener] is resumed with the value
   * returned by its [onUnlock][OnUnlockListener.onUnlock] callback.
   *
   * @param T Value returned by the [listener]'s callback.
   * @param listener [OnUnlockListener] whose returned value will be awaited.
   */
  private suspend fun <T> awaitUnlock(listener: OnUnlockListener<T>): T {
    actorFlow.filterNotNull().take(1).collect()
    return requestUnlock(listener)
  }

  /**
   * Ensures that the operation in the [listener]'s callback is only performed when the [Actor] is
   * authenticated; if it isn't, then authentication is requested and, if it succeeds, the operation
   * is performed.
   *
   * @param T Value returned by the [listener]'s callback.
   * @param listener [OnUnlockListener] to be notified when the [Actor] is authenticated.
   * @return Result of the [listener]'s callback.
   * @throws FailedAuthenticationException If authentication fails.
   */
  @Throws(FailedAuthenticationException::class)
  private suspend fun <T> requestUnlock(listener: OnUnlockListener<T>): T {
    val unlock = requestUnlockWithProvidedActor(listener)
    actorFlow.emit(unlock.actor)
    requestScheduledUnlocks()
    return unlock.value
  }

  /**
   * Suspends until the [Actor] provided by the [actorProvider] is authenticated, requesting the
   * authentication process to be performed if it currently isn't. After it's finished, the
   * [listener] is notified.
   *
   * @param T Value returned by the [listener]'s callback.
   * @param listener [OnUnlockListener] to be notified when the [Actor] is authenticated.
   * @throws FailedAuthenticationException If authentication fails.
   */
  @Throws(FailedAuthenticationException::class)
  private suspend fun <T> requestUnlockWithProvidedActor(listener: OnUnlockListener<T>): Unlock<T> {
    return when (val actor = actorProvider.provide()) {
      is Actor.Unauthenticated -> authenticateAndUnlock(listener)
      is Actor.Authenticated -> Unlock(actor, listener.onUnlock(actor))
    }
  }

  /**
   * Authenticates and notifies the [listener] if the resulting [Actor] is authenticated.
   *
   * @param T Value returned by the [listener]'s callback.
   * @param listener [OnUnlockListener] to be notified when the [Actor] is authenticated.
   * @throws FailedAuthenticationException If authentication fails.
   */
  @Throws(FailedAuthenticationException::class)
  private suspend fun <T> authenticateAndUnlock(listener: OnUnlockListener<T>): Unlock<T> {
    return when (val actor = authenticator.authenticate()) {
      is Actor.Unauthenticated -> {
        actorFlow.value = null
        throw createFailedAuthenticationException()
      }
      is Actor.Authenticated -> Unlock(actor, listener.onUnlock(actor))
    }
  }

  /**
   * Requests scheduled unlocks to be performed, resuming their associated [Continuation]s with the
   * value returned by their [OnUnlockListener]. Since authentication is implied to have already
   * been performed by a previous unlock, the resulting [Actor] is reused.
   */
  private suspend fun requestScheduledUnlocks() {
    val actor = actorFlow.filterNotNull().first()
    for ((listener, continuation) in schedule) {
      val value = listener.onUnlock(actor)
      @Suppress("UNCHECKED_CAST") (continuation as Continuation<Any?>).resume(value)
      schedule.remove(listener)
    }
  }

  companion object
}
