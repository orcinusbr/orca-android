package com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication

import android.content.Context
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.auth.actor.Actor
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication.ui.AuthenticationActivity
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.authorization.MastodonAuthorizer
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MastodonAuthenticator(
    private val context: Context,
    override val authorizer: MastodonAuthorizer,
    override val actorProvider: ActorProvider
) : Authenticator() {
    private var continuation: Continuation<Actor>? = null

    override suspend fun onAuthenticate(authorizationCode: String): Actor {
        return suspendCoroutine {
            continuation = it
            AuthenticationActivity.start(context, authorizationCode)
        }
    }

    internal fun receive(actor: Actor) {
        continuation?.resume(actor)
    }
}
