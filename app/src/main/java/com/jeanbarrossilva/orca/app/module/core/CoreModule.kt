package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.injector.Injector
import com.jeanbarrossilva.orca.std.injector.module.Module

internal abstract class CoreModule : Module() {
    override val dependencies: Scope.() -> Unit = {
        inject { authenticationLock() }
        inject { termMuter() }
        inject { instanceProvider() }
    }

    protected abstract fun Injector.authenticationLock(): SomeAuthenticationLock

    protected abstract fun Injector.termMuter(): TermMuter

    protected abstract fun Injector.instanceProvider(): InstanceProvider
}
