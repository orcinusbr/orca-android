package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.injector.module.Dependency
import com.jeanbarrossilva.orca.std.injector.module.Module

internal abstract class CoreModule(
    @Dependency private val authenticationLock: Module.() -> SomeAuthenticationLock,
    @Dependency private val termMuter: Module.() -> TermMuter,
    @Dependency private val instanceProvider: Module.() -> InstanceProvider
) : Module()
