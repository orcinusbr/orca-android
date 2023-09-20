package com.jeanbarrossilva.orca.core.http.auth.authentication.authenticator

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticationActivity
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.platform.ui.core.ActivityStarter
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * [Authenticator] that starts the specified [HttpAuthenticationActivity] when the user is requested
 * to be authorized and suspends until [Actor] is received.
 *
 * @param T [HttpAuthenticationActivity] to be started.
 * @see receive
 **/
abstract class HttpAuthenticator<T : HttpAuthenticationActivity<*>>
@PublishedApi
internal constructor() : Authenticator() {
    /** [Continuation] of the coroutine that's suspended on authentication. **/
    private var continuation: Continuation<Actor>? = null

    /**
     * [ActivityStarterProvider] by which the [ActivityStarter] that starts the specified
     * [HttpAuthenticationActivity] will be provided.
     **/
    protected abstract val activityStarterProvider: ActivityStarterProvider<T>

    override suspend fun onAuthenticate(authorizationCode: String): Actor {
        return suspendCoroutine {
            continuation = it
            activityStarterProvider.provide(authorizationCode).asNewTask().start()
        }
    }

    /**
     * Notifies this [HttpAuthenticator] that the [actor] has been successfully retrieved,
     * consequently resuming the suspended coroutine.
     *
     * @param actor [Actor] to be received.
     **/
    internal fun receive(actor: Actor) {
        continuation?.resume(actor)
    }

    companion object {
        /**
         * Creates an [HttpAuthenticator].
         *
         * @param T [HttpAuthenticationActivity] to be started.
         * @param authorizer [HttpAuthorizer] with which the user will be authorized.
         * @param actorProvider [ActorProvider] to which the [authenticated][Actor.Authenticated]
         * [Actor] will be sent to be remembered when authentication occurs.
         * @param activityStarterProvider [ActivityStarterProvider] by which the [ActivityStarter]
         * that starts the specified [HttpAuthenticationActivity] will be provided.
         **/
        inline fun <reified T : HttpAuthenticationActivity<*>> of(
            authorizer: HttpAuthorizer<*>,
            actorProvider: ActorProvider,
            activityStarterProvider: ActivityStarterProvider<T>
        ): HttpAuthenticator<T> {
            return object : HttpAuthenticator<T>() {
                override val authorizer = authorizer
                override val actorProvider = actorProvider
                override val activityStarterProvider = activityStarterProvider
            }
        }
    }
}
