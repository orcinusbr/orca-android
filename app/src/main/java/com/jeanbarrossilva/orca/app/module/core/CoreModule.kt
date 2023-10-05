package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.injector.Injectable
import com.jeanbarrossilva.orca.std.injector.module.Module

internal abstract class CoreModule : Module() {
    protected abstract val authenticationLock: Injectable<SomeAuthenticationLock>
    protected abstract val termMuter: Injectable<TermMuter>
    protected abstract val instanceProvider: Injectable<InstanceProvider>

    override val dependencies: Scope.() -> Unit = {
        inject(authenticationLock)
        inject(termMuter)
        inject(instanceProvider)
    }
}
