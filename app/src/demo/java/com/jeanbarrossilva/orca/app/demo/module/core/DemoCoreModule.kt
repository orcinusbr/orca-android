package com.jeanbarrossilva.orca.app.demo.module.core

import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.muting.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.instance.sample
import com.jeanbarrossilva.orca.std.injector.Injector

internal object DemoCoreModule : CoreModule() {
    override fun Injector.authenticationLock(): SomeAuthenticationLock {
        return Instance.sample.authenticationLock
    }

    override fun Injector.termMuter(): TermMuter {
        return SampleTermMuter()
    }

    override fun Injector.instanceProvider(): InstanceProvider {
        return InstanceProvider.sample
    }
}
