package com.jeanbarrossilva.orca.core.http.auth.authorization

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.platform.ui.core.on
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KClass

/**
 * [Authorizer] that starts the specified [HttpAuthorizationActivity] when the user is requested to
 * be authorized and suspends until an access token is received.
 *
 * @param T [HttpAuthorizationActivity] to be started.
 * @see receive
 **/
abstract class HttpAuthorizer<T : HttpAuthorizationActivity<*>>
@PublishedApi
internal constructor() : Authorizer() {
    /** [Continuation] of the coroutine that's suspended on authorization. **/
    private var continuation: Continuation<String>? = null

    /** [Context] through which the [HttpAuthorizationActivity] will be started. **/
    protected abstract val context: Context

    /** [KClass] of the [HttpAuthorizationActivity]. **/
    protected abstract val activityClass: KClass<T>

    override suspend fun authorize(): String {
        return suspendCoroutine {
            continuation = it
            context.on(activityClass).asNewTask().start()
        }
    }

    /**
     * Notifies this [HttpAuthorizer] that the [accessToken] has been successfully retrieved,
     * consequently resuming the suspended coroutine.
     *
     * @param accessToken Access token to be received.
     **/
    internal fun receive(accessToken: String) {
        continuation?.resume(accessToken)
    }

    companion object {
        /**
         * Creates an [HttpAuthorizer].
         *
         * @param T [HttpAuthorizationActivity] to be started.
         * @param context [Context] through which the [HttpAuthorizationActivity] will be started.
         **/
        inline fun <reified T : HttpAuthorizationActivity<*>> of(context: Context):
            HttpAuthorizer<T> {
            return object : HttpAuthorizer<T>() {
                override val context = context
                override val activityClass = T::class
            }
        }
    }
}
