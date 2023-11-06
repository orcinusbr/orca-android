package com.jeanbarrossilva.orca.app.module.core

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.core.http.instance.HttpInstanceProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.orca.core.sharedpreferences.feed.profile.toot.content.SharedPreferencesTermMuter
import com.jeanbarrossilva.orca.std.imageloader.compose.CoilImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector

internal object MainHttpModule :
  HttpModule(
    {
      HttpInstanceProvider(
        context = Injector.get(),
        MainHttpModule.actorProvider,
        MainHttpModule.authenticationLock,
        CoilImageLoader.Provider(MainHttpModule.context)
      )
    },
    { MainHttpModule.authenticationLock },
    { SharedPreferencesTermMuter(MainHttpModule.context) }
  ) {
  private val authenticationLock
    get() = AuthenticationLock(authenticator, actorProvider)

  private val authenticator
    get() = HttpAuthenticator(context, authorizer, actorProvider)

  private val actorProvider
    get() = SharedPreferencesActorProvider(context)

  private val authorizer
    get() = HttpAuthorizer(context)

  private val context
    get() = Injector.get<Context>()
}
