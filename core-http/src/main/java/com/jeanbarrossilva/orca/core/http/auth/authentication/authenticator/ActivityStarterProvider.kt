package com.jeanbarrossilva.orca.core.http.auth.authentication.authenticator

import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticationActivity
import com.jeanbarrossilva.orca.platform.ui.core.ActivityStarter

/**
 * Provides an [ActivityStarter] by which the [HttpAuthenticationActivity] will be started through
 * [provide].
 **/
fun interface ActivityStarterProvider<T : HttpAuthenticationActivity<*>> {
    /**
     * Provides an [ActivityStarter] for the specified [HttpAuthenticationActivity].
     *
     * @param authorizationCode Code resulted from a successful authorization.
     **/
    fun provide(authorizationCode: String): ActivityStarter<T>
}
