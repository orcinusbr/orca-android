package com.jeanbarrossilva.orca.core.http.instance

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.http.HttpBridge
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.viewmodel.HttpAuthorizationViewModel
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
    override fun provide(): SomeInstance {
        @Suppress("UNCHECKED_CAST")
        return ContextualHttpInstance(
            context,
            HttpAuthorizationViewModel.getInstanceDomain(context),
            authorizer = Injector.get(),
            authenticator = Injector.get(),
            actorProvider = Injector.get(),
            Injector.get<SomeAuthenticationLock>() as AuthenticationLock<HttpAuthenticator>
        )
            .also(HttpBridge::cross)
    }
}
