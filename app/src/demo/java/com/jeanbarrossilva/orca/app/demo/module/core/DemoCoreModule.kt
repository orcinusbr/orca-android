package com.jeanbarrossilva.orca.app.demo.module.core

import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.muting.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.instance.sample

internal object DemoCoreModule : CoreModule(
    { Instance.sample.authenticationLock },
    { SampleTermMuter() },
    { InstanceProvider.sample }
)
