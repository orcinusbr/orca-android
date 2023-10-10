package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.core.http.instance.HttpInstanceProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.toot.muting.SharedPreferencesTermMuter

internal class MainCoreModule : CoreModule(
    { AuthenticationLock(get<HttpAuthenticator>(), actorProvider = get()) },
    { SharedPreferencesTermMuter(context = get()) },
    { HttpInstanceProvider(context = get()) }
) {
    init {
        inject { HttpAuthorizer(context = get()) }
        inject<ActorProvider> { SharedPreferencesActorProvider(context = get()) }
        inject { HttpAuthenticator(context = get(), get<HttpAuthorizer>(), actorProvider = get()) }
    }
}
