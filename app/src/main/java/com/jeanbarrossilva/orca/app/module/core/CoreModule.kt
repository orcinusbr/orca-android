package com.jeanbarrossilva.orca.app.module.core

import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.std.injector.Injectable
import com.jeanbarrossilva.orca.std.injector.Injector

internal abstract class CoreModule {
    protected abstract val authenticationLock: Injectable<SomeAuthenticationLock>
    protected abstract val instanceProvider: Injectable<InstanceProvider>

    fun inject() {
        Injector.inject(authenticationLock)
        Injector.inject(instanceProvider)
    }
}
