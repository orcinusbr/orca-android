package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.http.HttpModule
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
        SharedPreferencesActorProvider(context = Injector.get()),
        CoilImageLoader.Provider(context = Injector.get())
      )
    },
    { SharedPreferencesTermMuter(context = Injector.get()) }
  )
