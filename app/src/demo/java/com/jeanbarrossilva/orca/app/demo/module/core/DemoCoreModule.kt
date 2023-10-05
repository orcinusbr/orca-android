package com.jeanbarrossilva.orca.app.demo.module.core

import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.muting.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.instance.sample
import com.jeanbarrossilva.orca.std.injector.Injectable

internal object DemoCoreModule : CoreModule() {
    override val authenticationLock =
        Injectable<SomeAuthenticationLock> { Instance.sample.authenticationLock }
    override val termMuter = Injectable { SampleTermMuter() }
    override val instanceProvider = Injectable { InstanceProvider.sample }
}
