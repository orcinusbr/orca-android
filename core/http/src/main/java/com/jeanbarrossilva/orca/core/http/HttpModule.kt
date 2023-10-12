package com.jeanbarrossilva.orca.core.http

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

open class HttpModule(
  @Inject internal val authorizer: Module.() -> Authorizer,
  @Inject internal val authenticator: Module.() -> Authenticator,
  @Inject internal val actorProvider: Module.() -> ActorProvider,
  @Inject internal val authenticationLock: Module.() -> SomeAuthenticationLock,
  @Inject internal val termMuter: Module.() -> TermMuter,
  @Inject internal val instanceProvider: Module.() -> InstanceProvider
) : Module()
