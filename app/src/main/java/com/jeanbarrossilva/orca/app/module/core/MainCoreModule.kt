package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.core.http.instance.HttpInstanceProvider
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.orca.std.injector.Injectable

internal class MainCoreModule : CoreModule() {
    override val dependencies: Scope.() -> Unit = {
        inject { HttpAuthorizer(context = get()) }
        inject<ActorProvider> { SharedPreferencesActorProvider(context = get()) }
        inject { HttpAuthenticator(context = get(), authorizer = get(), actorProvider = get()) }
        super.dependencies(this)
    }

    override val authenticationLock = Injectable<SomeAuthenticationLock> {
        AuthenticationLock(authenticator = get(), actorProvider = get())
    }
    override val instanceProvider =
        Injectable<InstanceProvider> { HttpInstanceProvider(context = get()) }
}
