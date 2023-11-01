package com.jeanbarrossilva.orca.core.sample

import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.sample.auth.SampleAuthenticator
import com.jeanbarrossilva.orca.core.sample.auth.SampleAuthorizer
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.image.SampleImageSource
import com.jeanbarrossilva.orca.core.sample.instance.sample
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

open class SampleCoreModule(
  @Inject internal val imageLoaderProvider: Module.() -> ImageLoader.Provider<SampleImageSource>
) :
  CoreModule(
    { SampleAuthorizer },
    { SampleAuthenticator },
    { ActorProvider.sample },
    { AuthenticationLock(authenticator = get(), actorProvider = get()) },
    { SampleTermMuter() },
    { InstanceProvider.sample },
    imageLoaderProvider
  )
