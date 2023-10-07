package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.injector.Module

internal abstract class CoreModule(
    authenticationLock: Module.() -> SomeAuthenticationLock,
    termMuter: Module.() -> TermMuter,
    instanceProvider: Module.() -> InstanceProvider
) : Module() {
    init {
        inject(authenticationLock)
        inject(termMuter)
        inject(instanceProvider)
    }
}
