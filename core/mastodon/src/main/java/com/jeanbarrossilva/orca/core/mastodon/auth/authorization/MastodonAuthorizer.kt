package com.jeanbarrossilva.orca.core.mastodon.auth.authorization

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.ui.AuthorizationActivity
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MastodonAuthorizer(private val context: Context) : Authorizer() {
    private var continuation: Continuation<String>? = null

    override suspend fun authorize(): String {
        return suspendCoroutine {
            continuation = it
            AuthorizationActivity.start(context)
        }
    }

    internal fun receive(accessToken: String) {
        continuation?.resume(accessToken)
    }
}
