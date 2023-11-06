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
        MainHttpModule.authorizer,
        MainHttpModule.authenticator,
        MainHttpModule.actorProvider,
        MainHttpModule.authenticationLock,
        MainHttpModule.termMuter,
        CoilImageLoader.Provider(MainHttpModule.context)
      )
    },
    { MainHttpModule.authenticationLock },
    { MainHttpModule.termMuter }
  ) {
  private val actorProvider by lazy { SharedPreferencesActorProvider(context) }
  private val authorizer by lazy { HttpAuthorizer(context) }
  private val authenticator by lazy { HttpAuthenticator(context, authorizer, actorProvider) }
  private val authenticationLock by lazy { AuthenticationLock(authenticator, actorProvider) }
  private val termMuter by lazy { SharedPreferencesTermMuter(context) }

  private val context
    get() = Injector.get<Context>()
}
