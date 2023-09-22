package com.jeanbarrossilva.orca.feature.auth.test

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.instance.sample
import org.koin.core.module.Module
import org.koin.dsl.module

@Suppress("TestFunctionName")
internal fun AuthModule(): Module {
    return module {
        single {
            Instance.sample
        }
    }
}
