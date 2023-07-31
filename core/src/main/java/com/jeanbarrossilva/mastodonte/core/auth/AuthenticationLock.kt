package com.jeanbarrossilva.mastodonte.core.auth

import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider

/**
 * Ensures that an operation is only performed either by...
 *
 * - ...an [unauthenticated][Actor.Unauthenticated] [Actor], through [requestLock];
 * - ...an [authenticated][Actor.Authenticated] [Actor], through [requestUnlock].
 *
 * @param authenticator [Authenticator] through which the [Actor] will be requested to be either
 * [authenticated][Actor.Authenticated] or [unauthenticated][Actor.Unauthenticated].
 * @param actorProvider [ActorProvider] whose provided [Actor] will be ensured to be either
 * [unauthenticated][Actor.Unauthenticated] or [authenticated][Actor.Authenticated].
 **/
class AuthenticationLock(
    private val authenticator: Authenticator,
    private val actorProvider: ActorProvider
) {
    /** [IllegalStateException] thrown if authentication fails. **/
    class FailedAuthenticationException internal constructor() :
        IllegalStateException("Could not authenticate properly.")

    /** Listens to a lock. **/
    fun interface OnLockListener {
        /**
         * Callback called when the [Actor] provided by the [actorProvider] is
         * [unauthenticated][Actor.Unauthenticated].
         *
         * @param actor Provided [unauthenticated][Actor.Unauthenticated] [Actor].
         **/
        fun onLock(actor: Actor.Unauthenticated)
    }

    /**
     * Listens to an unlock.
     *
     * @param T Value returned by [onUnlock].
     **/
    fun interface OnUnlockListener<T> {
        /**
         * Callback called when the [Actor] provided by the [actorProvider] is
         * [authenticated][Actor.Authenticated].
         *
         * @param actor Provided [authenticated][Actor.Authenticated] [Actor].
         **/
        suspend fun onUnlock(actor: Actor.Authenticated): T
    }

    /**
     * Ensures that the operation in the [listener]'s [onLock][OnLockListener.onLock] callback is
     * only performed if the [Actor] is [unauthenticated][Actor.Unauthenticated].
     *
     * @param listener [OnLockListener] to be notified if the [Actor] is
     * [unauthenticated][Actor.Unauthenticated].
     **/
    suspend fun requestLock(listener: OnLockListener) {
        val actor = actorProvider.provide()
        if (actor is Actor.Unauthenticated) {
            listener.onLock(actor)
        }
    }

    /**
     * Ensures that the operation in the [listener]'s [onUnlock][OnUnlockListener.onUnlock] callback
     * is only performed if the [Actor] is [authenticated][Actor.Authenticated]; if it isn't, then
     * authentication is requested and, if it succeeds, the operation is performed.
     *
     * @param T Value returned by the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
     * @param listener [OnUnlockListener] to be notified if the [Actor] is
     * [authenticated][Actor.Authenticated].
     * @return Result of the [listener]'s [onUnlock][OnUnlockListener.onUnlock].
     * @throws FailedAuthenticationException If authentication fails.
     **/
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
     * [authenticated][Actor.Authenticated].
     * @throws FailedAuthenticationException If authentication fails.
     **/
    private suspend fun <T> authenticateAndNotify(listener: OnUnlockListener<T>): T {
        val actor = authenticator.authenticate()
        return if (actor is Actor.Authenticated) {
            listener.onUnlock(actor)
        } else {
            throw FailedAuthenticationException()
        }
    }
}
