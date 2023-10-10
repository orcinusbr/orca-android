package com.jeanbarrossilva.orca.core.http

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class HttpModule(
    @Inject private val authorizer: Module.() -> Authorizer,
    @Inject private val authenticator: Module.() -> Authenticator,
    @Inject private val actorProvider: Module.() -> ActorProvider,
    @Inject private val authenticationLock: Module.() -> SomeAuthenticationLock,
    @Inject private val termMuter: Module.() -> TermMuter,
    @Inject private val instanceProvider: Module.() -> InstanceProvider
) : Module()
