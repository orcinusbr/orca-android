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
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainCoreModule : CoreModule() {
    private val authorizer by lazy { HttpAuthorizer(context = Injector.get()) }
    private val actorProvider by lazy { SharedPreferencesActorProvider(context = Injector.get()) }
    private val authenticator by lazy {
        HttpAuthenticator(context = Injector.get(), authorizer, actorProvider)
    }

    override val authenticationLock = Injectable<SomeAuthenticationLock> {
        AuthenticationLock(authenticator, actorProvider = get())
    }
    override val instanceProvider =
        Injectable<InstanceProvider> { HttpInstanceProvider(context = get()) }

    init {
        Injector.inject(authorizer)
        Injector.inject<ActorProvider>(actorProvider)
        Injector.inject(authenticator)
    }
}
