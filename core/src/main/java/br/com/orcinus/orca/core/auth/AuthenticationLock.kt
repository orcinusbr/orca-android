/*
 * Copyright © 2023–2025 Orcinus
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
import br.com.orcinus.orca.std.func.monad.Maybe
import kotlin.coroutines.Continuation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take

/** An [AuthenticationLock] with a generic [Authenticator]. */
typealias SomeAuthenticationLock = AuthenticationLock<*>

/**
 * Lock for ensuring that an operation is only executed in case — and, if not, when — the current
 * [Actor] is authenticated. This process of either authenticating and executing an operation or
 * solely doing the latter due to the [Actor] already being authenticated is referred to here as
 * _unlocking_. Upon an unlock, information such as the ID, name and avatar of the [Actor] can be
 * obtained, and the aforementioned operation can be executed on their behalf.
 *
 * An unlock is performed via scheduling, so it might not be immediate: requesting to schedule one
 * suspends until every unlock prior to it finishes and, only then, it is performed. The unlock upon
 * which authentication may occur is called a _primary_ unlock; subsequent ones to which the
 * authenticated [Actor] is passed to are _secondary_ ones. In a sense, this structure behaves like
 * a queue.
 *
 * E. g.,
 * ```kotlin
 * scheduleUnlock { actor -> println("Unlock #1") }
 *   .flatMap { scheduleUnlock { actor -> delay(2.seconds); println("Unlock #2") } }
 *   .flatMap { scheduleUnlock { actor -> println("Unlock #3") } }
 * ```
 *
 * In such scenario in which the [Actor] is unauthenticated by the time the first unlock is
 * scheduled, they would be authenticated on the first unlock, and "Unlock #1" would get printed;
 * the authenticated [Actor] is, then, reused and passed into the next unlocks: the second one
 * delays for 2 s — interval in which nothing occurs and the third is on hold —, printing
 * "Unlock #2" after that; succeeding it comes the printing of "Unlock #3".
 *
 * Notice that the authentication process is loosely fallible: can fail not due to it resulting in
 * an explicit error (since that is not expected), but because the [authenticator] may return an
 * unauthenticated [Actor] when requested to authenticate. If it does, the primary unlock and all
 * those which depended on it (that is: were waiting for it to complete normally) are cancelled;
 * after such failure, an attempt to properly authenticate can be made again by scheduling another
 * unlock.
 *
 * @param A [Authenticator] to authenticate the [Actor] with.
 * @see scheduleUnlock
 */
abstract class AuthenticationLock<A : Authenticator> @InternalCoreApi constructor() {
  /**
   * Authenticated [Actor] which has been obtained from the [actorProvider] before an initial
   * unlock.
   */
  private val actorFlow = MutableStateFlow<Actor.Authenticated?>(null)

  /** [OnUnlockListener]s of unlocks that are awaiting the one being currently performed. */
  private val schedule = mutableListOf<OnUnlockListener<*>>()

  /**
   * [Authorizer] by which the [Actor] will be authorized and from whose resulting authentication
   * code they are authenticated.
   */
  protected abstract val authorizer: Authorizer

  /** [Authenticator] through which the [Actor] will be requested to be authenticated. */
  protected abstract val authenticator: A

  /** [ActorProvider] whose provided [Actor] will be ensured to be authenticated. */
  protected abstract val actorProvider: ActorProvider

  /**
   * Result of a successful unlock.
   *
   * @param R Type of the [value].
   * @param actor [Actor] that has been authenticated.
   * @param value [R] that has been returned by the [OnUnlockListener].
   */
  private class Unlock<R>(val actor: Actor.Authenticated, val value: R)

  /** [IllegalStateException] resulted from authentication failure. */
  class FailedAuthenticationException @InternalCoreApi constructor(override val cause: Throwable?) :
    IllegalStateException("Authentication has failed.")

  /**
   * Ensures that the operation in the callback of the [listener] is only performed when the [Actor]
   * is authenticated; if it is not, then authentication is requested and, if it succeeds, the
   * operation is performed.
   *
   * If an unlock has already been requested and is still ongoing, this one is queued for it to be
   * run as soon as the current finishes (suspending the flow of execution of this method until
   * then), reusing the [Actor] that has been first obtained from the [actorProvider] by the primary
   * unlock.
   *
   * @param R Value returned by the callback of the [listener].
   * @param listener [OnUnlockListener] to be notified when the [Actor] is authenticated.
   */
  suspend fun <R> scheduleUnlock(listener: OnUnlockListener<R>) =
    actorFlow.value?.let { awaitUnlock(listener) } ?: requestUnlock(listener)

  /** Creates a variant-specific [FailedAuthenticationException]. */
  protected abstract fun createFailedAuthenticationException(): FailedAuthenticationException

  /**
   * Callback run when an unlock is performed.
   *
   * @param actor Authenticated [Actor] resulted from the authentication performed by the unlock to
   *   which this listener listened or a previous one.
   */
  protected abstract suspend fun onUnlock(actor: Actor.Authenticated)

  /**
   * Suspends until the [Continuation] associated to the given [listener] is resumed with the value
   * returned by its [onUnlock][OnUnlockListener.onUnlock] callback.
   *
   * @param R Value returned by the callback of the [listener].
   * @param listener [OnUnlockListener] whose returned value will be awaited.
   */
  private suspend fun <R> awaitUnlock(
    listener: OnUnlockListener<R>
  ): Maybe<FailedAuthenticationException, R> {
    actorFlow.filterNotNull().take(1).collect()
    return requestUnlock(listener)
  }

  /**
   * Ensures that the operation in the callback of the [listener] is only performed when the [Actor]
   * is authenticated; if it is not, then authentication is requested and, if it succeeds, the
   * operation is performed.
   *
   * @param R Value returned by the callback of the [listener].
   * @param listener [OnUnlockListener] to be notified when the [Actor] is authenticated.
   */
  private suspend fun <R> requestUnlock(
    listener: OnUnlockListener<R>
  ): Maybe<FailedAuthenticationException, R> {
    val unlock = requestUnlockWithProvidedActor(listener).onSuccessful { actorFlow.emit(it.actor) }
    requestScheduledUnlocks()
    return unlock.map(Unlock<R>::value)
  }

  /**
   * Suspends until the [Actor] provided by the [actorProvider] is authenticated, requesting the
   * authentication process to be performed if it currently is not. After it is finished, the
   * [listener] is notified.
   *
   * @param R Value returned by the callback of the [listener].
   * @param listener [OnUnlockListener] to be notified when the [Actor] is authenticated.
   */
  private suspend fun <R> requestUnlockWithProvidedActor(
    listener: OnUnlockListener<R>
  ): Maybe<FailedAuthenticationException, Unlock<R>> {
    return when (val actor = actorProvider.provide()) {
      is Actor.Unauthenticated -> authenticateAndUnlock(listener)
      is Actor.Authenticated -> Maybe.successful(Unlock(actor, listener.onUnlock(actor)))
    }
  }

  /**
   * Authenticates and notifies the [listener] if the resulting [Actor] is authenticated.
   *
   * @param R Value returned by the callback of the [listener].
   * @param listener [OnUnlockListener] to be notified when the [Actor] is authenticated.
   */
  private suspend fun <R> authenticateAndUnlock(
    listener: OnUnlockListener<R>
  ): Maybe<FailedAuthenticationException, Unlock<R>> {
    return when (
      val actor = authenticator.authenticate(authorizationCode = authorizer.authorize())
    ) {
      is Actor.Unauthenticated -> {
        actorFlow.value = null
        Maybe.failed(createFailedAuthenticationException())
      }
      is Actor.Authenticated -> Maybe.successful(Unlock(actor, listener.onUnlock(actor)))
    }
  }

  /**
   * Requests scheduled unlocks to be performed, resuming their associated [Continuation]s with the
   * value returned by their [OnUnlockListener]. Since authentication is implied to have already
   * been performed by a previous unlock, the resulting [Actor] is reused.
   */
  private suspend fun requestScheduledUnlocks() {
    val actor = actorFlow.value ?: return
    onUnlock(actor)
    for (listener in schedule) {
      listener.onUnlock(actor)
      schedule.remove(listener)
    }
  }

  companion object
}
