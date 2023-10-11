package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.core.http.instance.HttpInstanceProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.toot.muting.SharedPreferencesTermMuter
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainHttpModule : HttpModule(
    { HttpAuthorizer(context = Injector.get()) },
    { HttpAuthenticator(context = Injector.get(), authorizer = get(), actorProvider = get()) },
    { SharedPreferencesActorProvider(context = Injector.get()) },
    { AuthenticationLock(authenticator = get(), actorProvider = get()) },
    { SharedPreferencesTermMuter(context = Injector.get()) },
    { HttpInstanceProvider(context = Injector.get()) }
)
