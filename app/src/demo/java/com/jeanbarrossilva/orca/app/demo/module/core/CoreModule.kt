package com.jeanbarrossilva.orca.app.demo.module.core

import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.sample.instance.sample
import org.koin.core.module.Module

@Suppress("FunctionName")
internal fun DemoCoreModule(): Module {
    return CoreModule(Instance.sample.authenticationLock, InstanceProvider.sample)
}
