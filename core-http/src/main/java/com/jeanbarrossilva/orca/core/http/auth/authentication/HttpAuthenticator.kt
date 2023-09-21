package com.jeanbarrossilva.orca.core.http.auth.authentication

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.http.auth.authentication.activity.HttpAuthenticationActivity
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * [Authenticator] that starts an [HttpAuthenticationActivity] when the user is requested to be
 * authenticated and suspends until [Actor] is received.
 *
 * @param context [Context] in which the [HttpAuthenticationActivity] will be started.
 * @see receive
 **/
class HttpAuthenticator(
    private val context: Context,
    override val authorizer: Authorizer,
    override val actorProvider: ActorProvider
) : Authenticator() {
    /** [Continuation] of the coroutine that's suspended on authentication. **/
    private var continuation: Continuation<Actor>? = null

    override suspend fun onAuthenticate(authorizationCode: String): Actor {
        return suspendCoroutine {
            continuation = it
            HttpAuthenticationActivity.start(context, authorizationCode)
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
}
