package com.jeanbarrossilva.mastodonte.core.auth

import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider

/**
 * Ensures that an operation is only performed either by...
 *
 * - ...an [unauthenticated][Actor.Unauthenticated] [Actor], through [lock];
 * - ...an [authenticated][Actor.Authenticated] [Actor], through [unlock].
 *
 * @param authorizer [Authorizer] by which [authenticator]'s authentication will be authorized.
 * @param authenticator [Authenticator] through which the [Actor] will be authenticated if it isn't
 * and [unlock] is called.
 * @param actorProvider [ActorProvider] whose provided [Actor] will be ensured to be either
 * [unauthenticated][Actor.Unauthenticated] or [authenticated][Actor.Authenticated].
 **/
class AuthenticationLock(
    private val authorizer: Authorizer,
    private val authenticator: Authenticator,
    private val actorProvider: ActorProvider
) {
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

    /** Listens to an unlock. **/
    fun interface OnUnlockListener {
        /**
         * Callback called when the [Actor] provided by the [actorProvider] is
         * [authenticated][Actor.Authenticated].
         *
         * @param actor Provided [authenticated][Actor.Authenticated] [Actor].
         **/
        fun onUnlock(actor: Actor.Authenticated)
    }

    /**
     * Ensures that the operation in the [listener]'s [onLock][OnLockListener.onLock] callback is
     * only performed if the [Actor] is [unauthenticated][Actor.Unauthenticated].
     *
     * @param listener [OnLockListener] to be notified if the [Actor] is
     * [unauthenticated][Actor.Unauthenticated].
     **/
    suspend fun lock(listener: OnLockListener) {
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
     * @param listener [OnUnlockListener] to be notified if the [Actor] is
     * [authenticated][Actor.Authenticated].
     **/
    suspend fun unlock(listener: OnUnlockListener) {
        when (val actor = actorProvider.provide()) {
            is Actor.Unauthenticated -> authenticateAndNotify(listener)
            is Actor.Authenticated -> listener.onUnlock(actor)
        }
    }

    /**
     * Authenticates and notifies the [listener] if the resulting [Actor] is
     * [authenticated][Actor.Authenticated].
     *
     * @param listener [OnUnlockListener] to be notified if the [Actor] is
     * [authenticated][Actor.Authenticated].
     **/
    private suspend fun authenticateAndNotify(listener: OnUnlockListener) {
        val actor = authenticator.authenticate(authorizer)
        if (actor is Actor.Authenticated) {
            listener.onUnlock(actor)
        }
    }
}
