package com.jeanbarrossilva.orca.app.module.core

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.core.http.get
import com.jeanbarrossilva.orca.core.http.instance.HttpInstanceProvider
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun MainCoreModule(): Module {
    val context = get<Context>()
    val actorProvider = SharedPreferencesActorProvider(context)
    val authorizer = HttpAuthorizer(context)
    val authenticator = HttpAuthenticator(context, authorizer, actorProvider)
    val authenticationLock = AuthenticationLock(authenticator, actorProvider)
    val instanceProvider = HttpInstanceProvider(context)
    return CoreModule(authenticationLock, instanceProvider).apply {
        single<ActorProvider> { actorProvider }
        single { authorizer }
        single { authenticator }
    }
}

@Suppress("FunctionName")
internal fun CoreModule(
    authenticationLock: SomeAuthenticationLock,
    instanceProvider: InstanceProvider
): Module {
    return module {
        single { authenticationLock }
        single { instanceProvider }
    }
}
