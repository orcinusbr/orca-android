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
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.updateAndGet
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/** An [AuthenticationLock] with a generic [Authenticator]. */
@Deprecated(
  "Authentication locks are no longer type-parameterized as of Orca 0.5.2.",
  ReplaceWith("AuthenticationLock", imports = ["br.com.orcinus.orca.core.auth.AuthenticationLock"])
)
typealias SomeAuthenticationLock = AuthenticationLock

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
 * @see scheduleUnlock
 */
abstract class AuthenticationLock @InternalCoreApi constructor() {
  /** [AtomicRef] to the deferred authenticated [Actor] obtained by a primary unlock. */
  private val actorDeferredRef =
    atomic<Deferred<Maybe<FailedAuthenticationException, Actor.Authenticated>>?>(null)

  /**
   * [Authorizer] by which the [Actor] will be authorized and from whose resulting authentication
   * code they are authenticated.
   */
  protected abstract val authorizer: Authorizer

  /** [Authenticator] through which the [Actor] will be requested to be authenticated. */
  protected abstract val authenticator: Authenticator

  /** [ActorProvider] whose provided [Actor] will be ensured to be authenticated. */
  protected abstract val actorProvider: ActorProvider

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
    actorDeferredRef
      .updateAndGet { actorDeferred ->
        if (actorDeferred == null || actorDeferred.isCompleted && actorDeferred.await().isFailed) {
          coroutineScope {
            async {
              (actorProvider.provide() as? Actor.Authenticated
                  ?: authenticator.authenticate(authorizer.authorize()) as? Actor.Authenticated)
                ?.let(Maybe.Companion::successful)
                ?: Maybe.failed(createFailedAuthenticationException())
            }
          }
        } else {
          actorDeferred
        }
      }
      .let { it as Deferred<Maybe<FailedAuthenticationException, Actor.Authenticated>> }
      .await()
      .onSuccessful { onUnlock(it) }
      .map { listener.onUnlock(it) }

  /** Creates a variant-specific [FailedAuthenticationException]. */
  protected abstract fun createFailedAuthenticationException(): FailedAuthenticationException

  /**
   * Callback run when an unlock is performed.
   *
   * @param actor Authenticated [Actor] resulted from the authentication performed by the unlock to
   *   which this listener listened or a previous one.
   */
  protected abstract suspend fun onUnlock(actor: Actor.Authenticated)

  companion object
}
