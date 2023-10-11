package com.jeanbarrossilva.orca.core.http.instance

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.actorProvider
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.core.http.auth.authorization.viewmodel.HttpAuthorizationViewModel
import com.jeanbarrossilva.orca.core.http.authenticationLock
import com.jeanbarrossilva.orca.core.http.authenticator
import com.jeanbarrossilva.orca.core.http.authorizer
import com.jeanbarrossilva.orca.core.http.termMuter
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.instance.SomeInstance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.std.injector.Injector

/**
 * [InstanceProvider] that provides a [ContextualHttpInstance].
 *
 * @param context [Context] through which the [Domain] of the [ContextualHttpInstance] will
 * retrieved.
 **/
class HttpInstanceProvider(private val context: Context) : InstanceProvider {
    private val module by lazy {
        Injector.from<HttpModule>()
    }

    override fun provide(): SomeInstance {
        @Suppress("UNCHECKED_CAST")
        return ContextualHttpInstance(
            context,
            HttpAuthorizationViewModel.getInstanceDomain(context),
            module.authorizer() as HttpAuthorizer,
            module.authenticator() as HttpAuthenticator,
            module.actorProvider(),
            module.authenticationLock() as AuthenticationLock<HttpAuthenticator>,
            module.termMuter()
        )
    }
}
