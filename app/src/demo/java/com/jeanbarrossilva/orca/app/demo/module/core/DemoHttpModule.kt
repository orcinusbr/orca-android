package com.jeanbarrossilva.orca.app.demo.module.core

import com.jeanbarrossilva.orca.app.module.core.sample.imageloader.SampleImageLoader
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.http.HttpModule
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.auth.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.muting.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.instance.sample
import com.jeanbarrossilva.orca.std.injector.Injector

internal object DemoHttpModule :
  HttpModule(
    { Authorizer.sample },
    { Instance.sample.authenticator },
    { ActorProvider.sample },
    { Instance.sample.authenticationLock },
    { SampleTermMuter() },
    { InstanceProvider.sample },
    { SampleImageLoader.Provider(context = Injector.get()) }
  )
