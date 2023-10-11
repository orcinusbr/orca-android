package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

internal abstract class CoreModule(
    @Inject internal val authenticationLock: Module.() -> SomeAuthenticationLock,
    @Inject internal val termMuter: Module.() -> TermMuter,
    @Inject internal val instanceProvider: Module.() -> InstanceProvider
) : Module()
